package cryptocatbox.binance.core.model

import cryptocatbox.common.domain.CurrencyPair
import java.math.BigDecimal

data class LimitOrderQuery(
    val symbol: CurrencyPair,
    val side: OrderSide,
    val price: BigDecimal,
    val quantity: BigDecimal,
    val reduceOnly: Boolean = false
)
