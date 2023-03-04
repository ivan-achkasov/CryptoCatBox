package cryptocatbox.exchange.binance.client.http.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class BatchFuturesOrdersRequest(
    @JsonProperty("batchOrders")
    val orders: List<FutureOrderRequest>
)
