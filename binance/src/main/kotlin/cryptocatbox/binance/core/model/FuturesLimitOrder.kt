package cryptocatbox.binance.core.model

import cryptocatbox.common.domain.CurrencyPair
import java.math.BigDecimal

data class FuturesLimitOrder(
    val id: Long,
    val exchangeId: Long,
    val symbol: CurrencyPair,
    val price: BigDecimal,
    val quantity: BigDecimal,
    val side: OrderSide,
    val status: OrderStatus
)
