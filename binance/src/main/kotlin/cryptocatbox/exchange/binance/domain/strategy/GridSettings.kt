package cryptocatbox.exchange.binance.domain.strategy

import cryptocatbox.common.domain.CurrencyPair
import java.math.BigDecimal

data class GridSettings(
    val numOfStartOrders: Int,
    val pair: CurrencyPair,
    val stepMultiplier: BigDecimal,
    val startPrice: BigDecimal,
    val sellOrderQuantity: BigDecimal,
    val buyOrderQuantity: BigDecimal
)