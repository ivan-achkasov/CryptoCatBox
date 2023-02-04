package criptocatbox.exchange.whitebit.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import criptocatbox.exchange.whitebit.client.dto.TickerPairData
import criptocatbox.exchange.whitebit.client.dto.TickersDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.net.URI

@Service
class WhiteBitHttpClient(
    val restTemplate: RestTemplate,
    @Value("\${whitebit.api.base_url}") val base_url: String,
    @Value("\${whitebit.api.prefix}") val prefix: String,
    @Value("\${whitebit.api.tickers}") val tickers_path: String
) {

    private val jsonMapper = ObjectMapper()

    @Cacheable("tickers")
    fun getTickers(): TickersDto {
        val responseNode = restTemplate.exchange(
            URI.create(base_url + prefix + tickers_path),
            HttpMethod.GET,
            HttpEntity.EMPTY,
            ObjectNode::class.java
        ).body!!

        val tickersData = mutableMapOf<String, TickerPairData>()
        responseNode.fields().forEach {
            if(!it.value["isFrozen"].asBoolean()) {
                tickersData[it.key] = TickerPairData(it.value["last_price"].asDouble())
            }
        }

        return TickersDto(tickersData)
    }
}