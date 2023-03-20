package cryptocatbox.binance.core.service.autoupdate

import cryptocatbox.binance.core.service.events.FuturesOrdersEventsManager
import cryptocatbox.binance.core.service.events.OrderUpdatedEvent
import cryptocatbox.binance.infrastructure.persistence.repository.FuturesOrderRepository
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Service
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Service
class FuturesOrdersAutoUpdateService(
    private val futuresOrdersEventsManager: FuturesOrdersEventsManager,
    private val futuresOrderRepository: FuturesOrderRepository
) : InitializingBean {
    private val workerPool: ExecutorService = Executors.newCachedThreadPool()

    override fun afterPropertiesSet() {
        futuresOrdersEventsManager.subscribe(this::updateOrder, FuturesOrdersEventsManager.Priority.FIRST)
    }

    private fun updateOrder(event: OrderUpdatedEvent) {
        //TODO: investigate which transaction level should to be used
        workerPool.submit {
            val order = futuresOrderRepository.findByExchangeId(event.orderId)
            order?.let {
                futuresOrderRepository.save(it.copy(status = event.status))
            }
        }
    }
}