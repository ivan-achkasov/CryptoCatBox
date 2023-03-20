package cryptocatbox.binance.infrastructure.persistence.repository

import cryptocatbox.binance.core.model.FuturesLimitOrder
import cryptocatbox.binance.infrastructure.persistence.dao.FuturesOrderDao
import cryptocatbox.binance.infrastructure.persistence.entity.FuturesOrderDbEntity
import cryptocatbox.common.domain.CurrencyPair
import org.springframework.stereotype.Repository

@Repository
open class FuturesOrderRepositoryImpl(private val futuresOrderDao: FuturesOrderDao) : FuturesOrderRepository {

    override fun save(order: FuturesLimitOrder): FuturesLimitOrder {
        val dbEntity = FuturesOrderDbEntity(
            order.id,
            order.exchangeId,
            order.symbol.buy,
            order.symbol.sell,
            order.price,
            order.quantity,
            order.side,
            order.status
        )
        val newEntity = futuresOrderDao.save(dbEntity)
        return FuturesLimitOrder(
            newEntity.id,
            newEntity.exchangeId,
            CurrencyPair(newEntity.baseAsset, newEntity.quoteAsset),
            newEntity.price,
            newEntity.quantity,
            newEntity.side,
            newEntity.status
        )
    }

    override fun findByExchangeId(exchangeId: Long): FuturesLimitOrder? {
        val foundEntity = futuresOrderDao.findByExchangeId(exchangeId)
        return foundEntity?.let {
            FuturesLimitOrder(
                it.id,
                it.exchangeId,
                CurrencyPair(it.baseAsset, it.quoteAsset),
                it.price,
                it.quantity,
                it.side,
                it.status
            )
        }
    }
}