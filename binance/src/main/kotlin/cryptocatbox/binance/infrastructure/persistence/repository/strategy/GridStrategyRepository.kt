package cryptocatbox.binance.infrastructure.persistence.repository.strategy

import cryptocatbox.binance.core.model.strategy.GridStrategy

interface GridStrategyRepository {
    fun save(strategy: GridStrategy): GridStrategy
    fun findById(id: Long): GridStrategy?
}