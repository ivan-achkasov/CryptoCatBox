package cryptocatbox.binance.infrastructure.persistence.repository.strategy

import cryptocatbox.binance.core.model.strategy.GridSettings
import cryptocatbox.binance.core.model.strategy.GridStrategy
import cryptocatbox.binance.infrastructure.persistence.dao.strategy.GridStrategyDao
import cryptocatbox.binance.infrastructure.persistence.entity.strategy.GridStrategyDbEntity
import cryptocatbox.common.domain.CurrencyPair
import org.springframework.stereotype.Repository

@Repository
open class GridStrategyRepositoryImpl(private val gridStrategyDao: GridStrategyDao) : GridStrategyRepository {

    override fun save(strategy: GridStrategy): GridStrategy {
        val dbEntity = GridStrategyDbEntity(
            strategy.id,
            strategy.startPrice,
            strategy.settings.symbol.buy,
            strategy.settings.symbol.sell,
            strategy.settings.stepDistinction,
            strategy.settings.sellOrderQuantity,
            strategy.settings.buyOrderQuantity,
            strategy.settings.numOfOpenOrders
        )

        val newEntity = gridStrategyDao.save(dbEntity)

        return GridStrategy(
            newEntity.id,
            newEntity.startPrice,
            GridSettings(
                CurrencyPair(newEntity.baseAsset, newEntity.quoteAsset),
                newEntity.stepDistinction,
                newEntity.sellOrderQuantity,
                newEntity.buyOrderQuantity,
                newEntity.numOfOpenOrders
            )
        )
    }

    override fun findById(id: Long): GridStrategy? {
        val foundEntity = gridStrategyDao.findById(id)
        return foundEntity?.let {
            GridStrategy(
                it.id,
                it.startPrice,
                GridSettings(
                    CurrencyPair(it.baseAsset, it.quoteAsset),
                    it.stepDistinction,
                    it.sellOrderQuantity,
                    it.buyOrderQuantity,
                    it.numOfOpenOrders
                )
            )
        }
    }
}
