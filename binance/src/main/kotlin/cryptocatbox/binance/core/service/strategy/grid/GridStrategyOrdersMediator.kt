package cryptocatbox.binance.core.service.strategy.grid

import cryptocatbox.binance.core.model.OrderStatus
import cryptocatbox.binance.core.service.events.FuturesOrdersEventsManager
import cryptocatbox.binance.core.service.events.OrderUpdatedEvent
import cryptocatbox.binance.infrastructure.persistence.repository.strategy.GridStrategyOrderRepository
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Service
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Service
class GridStrategyOrdersMediator(
    private val futuresOrdersEventsManager: FuturesOrdersEventsManager,
    private val strategyService: GridStrategyService,
    private val gridStrategyOrderRepository: GridStrategyOrderRepository
) : InitializingBean {

    private val workerPool: ExecutorService = Executors.newCachedThreadPool()

    override fun afterPropertiesSet() {
        futuresOrdersEventsManager.subscribe(this::processOrderUpdate)
    }

    private fun processOrderUpdate(eventOrder: OrderUpdatedEvent) {
        workerPool.execute {
            val strategyOrder = gridStrategyOrderRepository.findByExchangeId(eventOrder.orderId) ?: return@execute
            if (eventOrder.status == OrderStatus.FILLED) {
                strategyService.processFilledOrder(strategyOrder)
            }
        }
    }

}