package cryptocatbox.exchange.binance.dto

import cryptocatbox.common.domain.CurrencyPair
import java.math.BigDecimal

data class LimitOrderQuery(
    val pair: CurrencyPair,
    val side: OrderSide,
    val price: BigDecimal,
    val quantity: BigDecimal,
    val reduceOnly: Boolean = false
)
