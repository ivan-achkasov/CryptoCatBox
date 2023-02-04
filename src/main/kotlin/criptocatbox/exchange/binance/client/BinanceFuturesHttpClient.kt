package criptocatbox.exchange.binance.client

import com.binance.connector.client.impl.SpotClientImpl
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import criptocatbox.exchange.binance.client.dto.ExchangeInfoDto
import criptocatbox.exchange.binance.client.dto.FutureOrderRequestDto
import criptocatbox.exchange.binance.client.dto.KLineDataDto
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.math.RoundingMode
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.Charset
import java.util.LinkedList
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


@Component
class BinanceFuturesHttpClient(
    private val restTemplate: RestTemplate,
    private val objectMapper: ObjectMapper
) {

    private val baseUrl = "https://www.binance.com/fapi/v1"
    private val exchangeInfo = "/exchangeInfo"
    private val continuousKlines = "/continuousKlines"
    private val orders = "/orders"
    private val batchOrders = "/batchOrders"

    private val apiKeyHeader = HttpHeaders().apply { this["X-MBX-APIKEY"] = Keys.API_KEY }

    private val pricePrecisions: Map<String, Int>
    private val quantityPrecision: Map<String, Int>

    init {
        pricePrecisions = getExchangeInfo().symbols.associate { "${it.baseAsset}${it.marginAsset}" to it.pricePrecision }
        quantityPrecision = getExchangeInfo().symbols.associate { "${it.baseAsset}${it.marginAsset}" to it.quantityPrecision }
    }


    fun getExchangeInfo(): ExchangeInfoDto {
        //TODO: improve
        Thread.sleep(600)
        return restTemplate.exchange(
            baseUrl + exchangeInfo,
            HttpMethod.GET,
            null,
            ExchangeInfoDto::class.java
        ).body!!
    }

    fun getContinuousKlines(
        symbol: String,
        interval: String,
        limit: Short = 500,
        startTime: Long? = null,
        endTime: Long? = null,
        contractType: String = "PERPETUAL"
    ): List<KLineDataDto> {
        //TODO: improve
        Thread.sleep(600)
        if (limit > 1500) {
            throw IllegalArgumentException("Limit is > than 1500")
        }
        val params = "pair=$symbol&contractType=$contractType&limit=$limit&interval=$interval" +
                if(startTime != null) "&startTime=$startTime" else "" +
                if(endTime != null) "&endTime=$endTime" else ""
        val rawData = restTemplate.exchange(
            "$baseUrl$continuousKlines?$params",
            HttpMethod.GET,
            null,
            String::class.java,
            symbol,
            contractType,
            interval
        ).body!!
        val parsedData = objectMapper.readValue(rawData, object : TypeReference<Array<Array<JsonNode>>>() {})
        if (parsedData.isEmpty()) {
            return emptyList()
        }

        val result = LinkedList<KLineDataDto>()
        for (i in 0..parsedData.size - 2) {
            val el = parsedData[i]
            result.add(KLineDataDto(el[0].asLong(), el[6].asLong(), el[1].asDouble(), el[4].asDouble(), true))
        }
        parsedData.last().let { el ->
            result.add(KLineDataDto(el[0].asLong(), el[6].asLong(), el[1].asDouble(), el[4].asDouble(), false))
        }
        return result
    }

    /*fun placeOrder(requests: List<FutureOrderRequestDto>) {
        val updatedRequests = requests.map {
            FutureOrderRequestDto(
                it.symbol,
                it.side,
                it.quantity?.toBigDecimal()?.setScale(quantityPrecision[it.symbol]!!, RoundingMode.HALF_UP)?.toDouble(),
                it.type,
                it.stopPrice?.toBigDecimal()?.setScale(pricePrecisions[it.symbol]!!, RoundingMode.HALF_UP)?.toDouble(),
                it.reduceOnly,
                it.positionSide
            )
        }
        val timestamp = SpotClientImpl().createMarket().time().takeLast(14).substringBefore("}").toLong()
        val params = "timestamp=$timestamp" +
                "&"
                "&batchOrders=${URLEncoder.encode(objectMapper.writeValueAsString(updatedRequests), Charset.forName("UTF-8"))}"
        val signature = calculateHMac(params)
        val uri = URI("$baseUrl$orders?$params&signature=$signature")
        val res = restTemplate.exchange(
            uri,
            HttpMethod.POST,
            HttpEntity<Any>(apiKeyHeader),
            String::class.java
        ).body!!
        println(res)
    }*/

    fun placeOrders(requests: List<FutureOrderRequestDto>) {
        val updatedRequests = requests.map {
            FutureOrderRequestDto(
                it.symbol,
                it.side,
                it.quantity?.toBigDecimal()?.setScale(quantityPrecision[it.symbol]!!, RoundingMode.HALF_UP)?.toDouble(),
                it.type,
                it.stopPrice?.toBigDecimal()?.setScale(pricePrecisions[it.symbol]!!, RoundingMode.HALF_UP)?.toDouble(),
                it.reduceOnly,
                it.positionSide,
                it.priceProtect,
                it.closePosition
            )
        }
        val timestamp = SpotClientImpl().createMarket().time().takeLast(14).substringBefore("}").toLong()
        val params = "timestamp=$timestamp" +
                "&batchOrders=${URLEncoder.encode(objectMapper.writeValueAsString(updatedRequests), Charset.forName("UTF-8"))}"
        val signature = calculateHMac(params)
        val uri = URI("$baseUrl$batchOrders?$params&signature=$signature")
        val res = restTemplate.exchange(
            uri,
            HttpMethod.POST,
            HttpEntity<Any>(apiKeyHeader),
            String::class.java
        ).body!!
        println(res)
    }

    private fun calculateHMac(data: String): String {
        val sha256Hmac = Mac.getInstance("HmacSHA256")
        sha256Hmac.init(SecretKeySpec(Keys.SECRET_KEY.toByteArray(), "HmacSHA256"))
        return sha256Hmac.doFinal(data.toByteArray(charset("UTF-8"))).toHex()
    }

    private fun ByteArray.toHex(): String = joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }

}