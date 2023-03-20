package cryptocatbox.binance.infrastructure.persistence.entity.strategy

import cryptocatbox.binance.infrastructure.persistence.entity.FuturesOrderDbEntity
import org.springframework.data.relational.core.mapping.Table

@Table(schema = "binance", name = "futures_order_to_strategy")
data class GridStrategyOrderDbEntity(
    val strategyId: Long,
    val order: FuturesOrderDbEntity
)
