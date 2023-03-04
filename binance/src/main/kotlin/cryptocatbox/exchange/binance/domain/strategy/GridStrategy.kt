package cryptocatbox.exchange.binance.domain.strategy

import java.util.UUID

data class GridStrategy(
    val id: UUID,
    val settings: GridSettings
)
