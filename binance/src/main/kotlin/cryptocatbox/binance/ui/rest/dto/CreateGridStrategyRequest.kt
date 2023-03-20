package cryptocatbox.binance.ui.rest.dto

import java.math.BigDecimal

data class CreateGridStrategyRequest(
    val symbol: String,
    val stepMultiplier: BigDecimal,
    val sellOrderQuantity: BigDecimal,
    val buyOrderQuantity: BigDecimal
)