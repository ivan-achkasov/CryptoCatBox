package cryptocatbox.binance.infrastructure.client.http.dto

data class CancelOrderByOrderIdRequest(
    val symbol: String,
    val orderId: Long
)
