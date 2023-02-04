package criptocatbox.exchange.pancakeswap.client

import criptocatbox.exchange.pancakeswap.client.dto.TopPairs
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.net.URI

@Service
class PancakeSwapHttpClient(
    val restTemplate: RestTemplate,
    @Value("\${pancakeswap.api.base_url}") val base_url: String,
    @Value("\${pancakeswap.api.prefix}") val prefix: String,
    @Value("\${pancakeswap.api.top_pairs}") val topPairsPath: String
) {

    @Cacheable("pancake_top_pairs")
    fun getTopPairs(): TopPairs {
        return restTemplate.exchange(
            URI.create(base_url + prefix + topPairsPath),
            HttpMethod.GET,
            HttpEntity.EMPTY,
            TopPairs::class.java
        ).body!!
    }
}