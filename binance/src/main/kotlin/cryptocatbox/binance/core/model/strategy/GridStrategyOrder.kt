package cryptocatbox.binance.core.model.strategy

import cryptocatbox.binance.core.model.FuturesLimitOrder

data class GridStrategyOrder(
    val strategyId: Long,
    val order: FuturesLimitOrder
)
