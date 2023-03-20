package cryptocatbox.binance.infrastructure.client.http.dto

import cryptocatbox.binance.infrastructure.client.dto.OrderStatus
import cryptocatbox.binance.infrastructure.client.dto.Side
import java.math.BigDecimal
import java.math.BigInteger

data class OrderResponse(
    val orderId: BigInteger,
    val side: Side,
    val status: OrderStatus,
    val price: BigDecimal
)
