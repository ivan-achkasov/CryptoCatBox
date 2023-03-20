package cryptocatbox.binance.infrastructure.persistence.dao

import cryptocatbox.binance.infrastructure.persistence.entity.FuturesOrderDbEntity
import org.springframework.data.repository.Repository

interface FuturesOrderDao : Repository<FuturesOrderDbEntity, Long> {
    fun save(order: FuturesOrderDbEntity): FuturesOrderDbEntity

    fun findByExchangeId(exchangeId: Long): FuturesOrderDbEntity?
}