package cryptocatbox.binance.core.model.strategy

import cryptocatbox.common.domain.CurrencyPair
import java.math.BigDecimal

data class GridSettings(
    val symbol: CurrencyPair,
    val stepDistinction: BigDecimal,
    val sellOrderQuantity: BigDecimal,
    val buyOrderQuantity: BigDecimal
)