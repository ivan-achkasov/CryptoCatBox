package cryptocatbox.exchange.binance.client.http

import com.binance.connector.client.utils.SignatureGenerator
import com.binance.connector.client.utils.UrlBuilder
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import cryptocatbox.exchange.binance.client.Keys
import cryptocatbox.exchange.binance.client.http.dto.BatchFuturesOrdersRequest
import cryptocatbox.exchange.binance.client.http.dto.ExchangeInfoResponse
import cryptocatbox.exchange.binance.client.http.dto.ListenKeyResponse
import cryptocatbox.exchange.binance.client.http.dto.OrderResponse
import cryptocatbox.exchange.binance.client.http.dto.FutureOrderRequest
import cryptocatbox.exchange.binance.client.ws.dto.KLineDataDto
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpMethod.POST
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.util.LinkedList
import kotlin.collections.LinkedHashMap


@Component
class FuturesHttpClient(
    private val restTemplate: RestTemplate,
) {

    companion object {
        private const val ORDER_PATH = "/order"
        private const val BATCH_ORDERS_PATH = "/batchOrders"
        private const val LISTEN_KEY_PATH = "/listenKey"

        private const val TIMESTAMP = "timestamp"
    }

    private val baseUrl = "https://www.binance.com/fapi/v1"
    private val exchangeInfoPath = "/exchangeInfo"
    private val continuousKlines = "/continuousKlines"

    private val objectMapper = ObjectMapper()

    private val apiKeyHeader = HttpHeaders().apply { this["X-MBX-APIKEY"] =
        Keys.API_KEY
    }

    fun getExchangeInfo(): ExchangeInfoResponse {
        return restTemplate.exchange("$baseUrl$exchangeInfoPath", HttpMethod.GET,null, ExchangeInfoResponse::class.java).body!!
    }

    fun getContinuousKlines(
        symbol: String,
        interval: String,
        limit: Short = 500,
        startTime: Long? = null,
        endTime: Long? = null,
        contractType: String = "PERPETUAL"
    ): List<KLineDataDto> {
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
            result.add(
                KLineDataDto(
                    el[0].asLong(),
                    el[6].asLong(),
                    el[1].asDouble(),
                    el[4].asDouble(),
                    true
                )
            )
        }
        parsedData.last().let { el ->
            result.add(
                KLineDataDto(
                    el[0].asLong(),
                    el[6].asLong(),
                    el[1].asDouble(),
                    el[4].asDouble(),
                    false
                )
            )
        }
        return result
    }

    fun placeOrders(request: BatchFuturesOrdersRequest): List<OrderResponse> {
        return post<List<OrderResponse>>(BATCH_ORDERS_PATH, request).body!!
    }

    fun placeOrder(orderRequest: FutureOrderRequest): OrderResponse {
        return post<OrderResponse>(ORDER_PATH, orderRequest).body!!
    }

    fun getListenKey(): String {
        return post<ListenKeyResponse>(LISTEN_KEY_PATH).body!!.listenKey
    }

    fun refreshCurrentListenKey() {
        put(LISTEN_KEY_PATH)
    }

    private fun <T> post(path: String, request: Any): ResponseEntity<T> {
        val url = buildUrl(path, request)
        return restTemplate.exchange(url, POST, HttpEntity<Any>(apiKeyHeader), object : ParameterizedTypeReference<T>() {});
    }

    private fun <T> post(path: String): ResponseEntity<T> {
        return restTemplate.exchange(baseUrl + path, POST, HttpEntity<Any>(apiKeyHeader), object : ParameterizedTypeReference<T>() {});
    }

    private fun put(path: String) {
        restTemplate.put(baseUrl + path, HttpEntity<Any>(apiKeyHeader))
    }

    private fun buildUrl(path: String, request: Any): String {
        val params: LinkedHashMap<String, Any> = objectMapper.convertValue(request)
        params[TIMESTAMP] = UrlBuilder.buildTimestamp()
        val queryString = UrlBuilder.joinQueryParameters(params)
        val signature = SignatureGenerator.getSignature(queryString, Keys.SECRET_KEY)
        return UrlBuilder.buildFullUrl(baseUrl, path, params, signature)
    }

}