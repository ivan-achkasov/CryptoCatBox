package cryptocatbox.binance.infrastructure.persistence.repository

import cryptocatbox.binance.core.model.FuturesLimitOrder

interface FuturesOrderRepository {
    fun save(order: FuturesLimitOrder): FuturesLimitOrder
    fun findByExchangeId(exchangeId: Long): FuturesLimitOrder?
}