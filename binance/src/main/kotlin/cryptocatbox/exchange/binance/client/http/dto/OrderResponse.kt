package cryptocatbox.exchange.binance.client.http.dto

import cryptocatbox.exchange.binance.client.dto.OrderStatus
import cryptocatbox.exchange.binance.client.dto.Side
import java.math.BigDecimal
import java.math.BigInteger

data class OrderResponse(
    val orderId: BigInteger,
    val side: Side,
    val status: OrderStatus,
    val price: BigDecimal
)
