package cryptocatbox.binance.core.service.strategy.grid

import cryptocatbox.binance.core.model.strategy.GridSettings
import cryptocatbox.binance.core.model.strategy.GridStrategyOrder

interface GridStrategyService {
    fun startNew(settings: GridSettings)
    fun processFilledOrder(gridOrder: GridStrategyOrder)
}