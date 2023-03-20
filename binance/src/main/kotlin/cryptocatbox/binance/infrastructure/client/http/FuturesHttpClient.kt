package cryptocatbox.binance.infrastructure.client.http

import com.binance.connector.client.utils.HmacSignatureGenerator
import com.binance.connector.client.utils.UrlBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import cryptocatbox.binance.infrastructure.client.Keys
import cryptocatbox.binance.infrastructure.client.http.dto.BatchFuturesOrdersRequest
import cryptocatbox.binance.infrastructure.client.http.dto.CancelOrderByOrderIdRequest
import cryptocatbox.binance.infrastructure.client.http.dto.ExchangeInfoResponse
import cryptocatbox.binance.infrastructure.client.http.dto.FutureOrderRequest
import cryptocatbox.binance.infrastructure.client.http.dto.ListenKeyResponse
import cryptocatbox.binance.infrastructure.client.http.dto.OrderResponse
import cryptocatbox.binance.infrastructure.client.http.dto.PremiumIndexRequest
import cryptocatbox.binance.infrastructure.client.http.dto.PremiumIndexResponse
import cryptocatbox.common.Logging
import cryptocatbox.common.logger
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpMethod.DELETE
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.POST
import org.springframework.http.HttpMethod.PUT
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.util.concurrent.atomic.AtomicLong

@Component
class FuturesHttpClient(
    private val restTemplate: RestTemplate,
) : Logging {

    private val reqIdProvider: AtomicLong = AtomicLong()

    companion object {
        private const val BASE_URL = "https://www.binance.com/fapi/v1"

        private const val ORDER_PATH = "/order"
        private const val BATCH_ORDERS_PATH = "/batchOrders"
        private const val LISTEN_KEY_PATH = "/listenKey"
        private const val PREMIUM_INDEX_PATH = "/premiumIndex"
        private const val EXCHANGE_INFO_PATH = "/exchangeInfo"

        private const val TIMESTAMP = "timestamp"
    }

    private val objectMapper = ObjectMapper()

    private val apiKeyHeader = HttpHeaders().apply {
        this["X-MBX-APIKEY"] =
            Keys.API_KEY
    }

    private val signatureGenerator = HmacSignatureGenerator(Keys.SECRET_KEY)

    fun getExchangeInfo(): ExchangeInfoResponse {
        return publicGet<ExchangeInfoResponse>(EXCHANGE_INFO_PATH).body!!
    }

    fun placeOrders(request: BatchFuturesOrdersRequest): List<OrderResponse> {
        return post<List<OrderResponse>>(BATCH_ORDERS_PATH, request).body!!
    }

    fun placeOrder(orderRequest: FutureOrderRequest): OrderResponse {
        return post<OrderResponse>(ORDER_PATH, orderRequest).body!!
    }

    fun cancelOrder(cancelOrderRequest: CancelOrderByOrderIdRequest) {
        return delete(ORDER_PATH, cancelOrderRequest)
    }

    fun getListenKey(): String {
        return post<ListenKeyResponse>(LISTEN_KEY_PATH).body!!.listenKey
    }

    fun refreshCurrentListenKey() {
        put(LISTEN_KEY_PATH)
    }

    fun getPremiumIndex(request: PremiumIndexRequest): PremiumIndexResponse {
        return publicGet<PremiumIndexResponse>(PREMIUM_INDEX_PATH, request).body!!
    }

    private inline fun <reified T> publicGet(path: String, request: Any? = null): ResponseEntity<T> {
        val url = buildPublicUrl(path, request)
        val requestId = generateRequestId()
        logRequest(requestId, url, GET)
        val response = restTemplate.exchange(url, GET, null, T::class.java)
        logResponse(requestId, response)
        return response
    }

    private inline fun <reified T> post(path: String, request: Any): ResponseEntity<T> {
        val url = buildSignedUrl(path, request)
        val requestId = generateRequestId()
        logRequest(requestId, url, POST)
        val response = restTemplate.exchange(
            url,
            POST,
            HttpEntity<Any>(apiKeyHeader),
            T::class.java
        )
        logResponse(requestId, response)
        return response
    }

    private inline fun <reified T> post(path: String): ResponseEntity<T> {
        val url = BASE_URL + path
        val requestId = generateRequestId()
        logRequest(requestId, url, POST)
        val response = restTemplate.exchange(
            url,
            POST,
            HttpEntity<Any>(apiKeyHeader),
            T::class.java
        )
        logResponse(requestId, response)
        return response
    }

    private fun put(path: String) {
        val url = BASE_URL + path
        val requestId = generateRequestId()
        logRequest(requestId, url, PUT)
        val response = restTemplate.execute<ResponseEntity<Any>>(
            url,
            PUT,
            restTemplate.httpEntityCallback<Any>(HttpEntity<Any>(apiKeyHeader)),
            restTemplate.responseEntityExtractor(Any::class.java)
        )
        logResponse(requestId, response)
    }

    private fun delete(path: String, request: Any) {
        val url = buildSignedUrl(path, request)
        val requestId = generateRequestId()
        logRequest(requestId, url, DELETE)
        val response = restTemplate.execute<ResponseEntity<Any>>(
            url,
            DELETE,
            restTemplate.httpEntityCallback<Any>(HttpEntity<Any>(apiKeyHeader)),
            restTemplate.responseEntityExtractor(Any::class.java)
        )
        logResponse(requestId, response)
    }

    private fun buildSignedUrl(path: String, request: Any): String {
        val params: LinkedHashMap<String, Any> = objectMapper.convertValue(request)
        params[TIMESTAMP] = UrlBuilder.buildTimestamp()
        val queryString = UrlBuilder.joinQueryParameters(params)
        val signature = signatureGenerator.getSignature(queryString)
        return UrlBuilder.buildFullUrl(BASE_URL, path, params, signature)
    }

    private fun buildPublicUrl(path: String, request: Any?): String {
        val params: LinkedHashMap<String, Any>? = request?.let { objectMapper.convertValue(it) }
        return UrlBuilder.buildFullUrl(BASE_URL, path, params, null)
    }

    private fun logRequest(reqId: Long, url: String, method: HttpMethod) {
        logger().info("Sending request to binance features API. Request id: {}. URL: [{}]:{}", reqId, method, url)
    }

    private fun <T> logResponse(reqId: Long, response: ResponseEntity<T>?) {
        logger().info(
            "Received features binance response for request id: {}. Status code: {}. Response: {}",
            reqId,
            response?.statusCode,
            response?.body
        )
    }

    private fun generateRequestId(): Long {
        return reqIdProvider.incrementAndGet()
    }

}