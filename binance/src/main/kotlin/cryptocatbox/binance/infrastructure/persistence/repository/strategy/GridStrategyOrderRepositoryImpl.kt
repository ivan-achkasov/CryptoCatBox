package cryptocatbox.binance.infrastructure.persistence.repository.strategy

import cryptocatbox.binance.core.model.FuturesLimitOrder
import cryptocatbox.binance.core.model.strategy.GridStrategyOrder
import cryptocatbox.binance.infrastructure.persistence.dao.strategy.GridStrategyOrderDao
import cryptocatbox.binance.infrastructure.persistence.entity.strategy.GridStrategyOrderDbEntity
import cryptocatbox.common.domain.CurrencyPair
import org.springframework.stereotype.Repository

@Repository
open class GridStrategyOrderRepositoryImpl(private val strategyOrderDao: GridStrategyOrderDao) :
    GridStrategyOrderRepository {

    override fun associateOrderWithStrategy(strategyId: Long, orderId: Long) {
        strategyOrderDao.associateOrderWithStrategy(strategyId, orderId)
    }

    override fun findLowerOpenSellOrderForStrategy(strategyId: Long): GridStrategyOrder? {
        return strategyOrderDao.findLowerOpenSellOrderForStrategy(strategyId)?.toDomainModel()
    }

    override fun findLowerOpenBuyOrderForStrategy(strategyId: Long): GridStrategyOrder? {
        return strategyOrderDao.findLowerOpenBuyOrderForStrategy(strategyId)?.toDomainModel()
    }

    override fun findHigherOpenBuyOrderForStrategy(strategyId: Long): GridStrategyOrder? {
        return strategyOrderDao.findHigherOpenBuyOrderForStrategy(strategyId)?.toDomainModel()
    }

    override fun findHigherOpenSellOrderForStrategy(strategyId: Long): GridStrategyOrder? {
        return strategyOrderDao.findHigherOpenSellOrderForStrategy(strategyId)?.toDomainModel()
    }

    override fun findByExchangeId(exchangeId: Long): GridStrategyOrder? {
        return strategyOrderDao.findByExchangeId(exchangeId)?.toDomainModel()
    }

    private fun GridStrategyOrderDbEntity.toDomainModel(): GridStrategyOrder {
        return GridStrategyOrder(
            this.strategyId,
            FuturesLimitOrder(
                this.order.id,
                this.order.exchangeId,
                CurrencyPair(this.order.baseAsset, this.order.quoteAsset),
                this.order.price,
                this.order.quantity,
                this.order.side,
                this.order.status

            )
        )
    }
}