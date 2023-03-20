package cryptocatbox.binance.core.model.strategy

import java.math.BigDecimal

data class GridStrategy(
    val id: Long,
    val startPrice: BigDecimal,
    val settings: GridSettings
)
