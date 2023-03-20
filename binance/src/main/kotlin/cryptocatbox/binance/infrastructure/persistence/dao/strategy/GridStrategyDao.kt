package cryptocatbox.binance.infrastructure.persistence.dao.strategy

import cryptocatbox.binance.infrastructure.persistence.entity.strategy.GridStrategyDbEntity
import org.springframework.data.repository.Repository

interface GridStrategyDao : Repository<GridStrategyDbEntity, Long> {
    fun save(strategy: GridStrategyDbEntity): GridStrategyDbEntity

    fun findById(id: Long): GridStrategyDbEntity?
}