package cryptocatbox.exchange.binance.dto

import java.math.BigDecimal

data class LimitOrder(
    val id: Long,
    val side: OrderSide,
    val status: OrderStatus,
    val price: BigDecimal
)
