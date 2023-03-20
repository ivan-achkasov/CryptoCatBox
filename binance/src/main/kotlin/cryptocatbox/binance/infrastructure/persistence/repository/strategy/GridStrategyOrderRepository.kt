package cryptocatbox.binance.infrastructure.persistence.repository.strategy

import cryptocatbox.binance.core.model.strategy.GridStrategyOrder

interface GridStrategyOrderRepository {

    fun associateOrderWithStrategy(strategyId: Long, orderId: Long)

    fun findLowerOpenSellOrderForStrategy(strategyId: Long): GridStrategyOrder?

    fun findLowerOpenBuyOrderForStrategy(strategyId: Long): GridStrategyOrder?

    fun findHigherOpenBuyOrderForStrategy(strategyId: Long): GridStrategyOrder?

    fun findHigherOpenSellOrderForStrategy(strategyId: Long): GridStrategyOrder?

    fun findByExchangeId(exchangeId: Long): GridStrategyOrder?

}