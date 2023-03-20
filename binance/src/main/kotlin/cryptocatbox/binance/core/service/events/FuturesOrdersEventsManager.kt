package cryptocatbox.binance.core.service.events

import com.fasterxml.jackson.databind.ObjectMapper
import cryptocatbox.binance.core.model.OrderStatus
import cryptocatbox.binance.core.model.of
import cryptocatbox.binance.infrastructure.client.dto.UserDataUpdateEventType
import cryptocatbox.binance.infrastructure.client.http.FuturesHttpClient
import cryptocatbox.binance.infrastructure.client.ws.BinanceFuturesWebSocketClient
import cryptocatbox.binance.infrastructure.client.ws.UserDataStreamsConnection
import cryptocatbox.binance.infrastructure.client.ws.dto.UserDataUpdateEvent
import cryptocatbox.common.Logging
import cryptocatbox.common.domain.parsePairWithoutDelimiter
import cryptocatbox.common.logger
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import java.util.Collections
import java.util.function.Consumer

@Component
class FuturesOrdersEventsManager(
    private val objectMapper: ObjectMapper,
    private val webSocket: BinanceFuturesWebSocketClient,
    private val httpClient: FuturesHttpClient
) : InitializingBean, Logging {
    private var firstPrioritySubscriber: Consumer<OrderUpdatedEvent>? = null
    private val orderUpdatedSubscribers = Collections.synchronizedList(mutableListOf<Consumer<OrderUpdatedEvent>>())

    private var currentConnection: UserDataStreamsConnection? = null

    override fun afterPropertiesSet() {
        openConnectionAndScheduleReopen()
        scheduleTokenRefresh()
    }

    fun subscribe(eventHandleMethod: Consumer<OrderUpdatedEvent>, priority: Priority = Priority.ANY) {
        when (priority) {
            Priority.FIRST -> setFirstSubscriber(eventHandleMethod)
            Priority.ANY -> orderUpdatedSubscribers.add(eventHandleMethod)
        }
    }

    private fun setFirstSubscriber(eventHandleMethod: Consumer<OrderUpdatedEvent>) {
        synchronized(this) {
            if (firstPrioritySubscriber != null) {
                throw IllegalArgumentException("Main first subscriber has been already set.")
            }
            firstPrioritySubscriber = eventHandleMethod
        }
    }

    private fun onMessage(rawEventData: String) {
        val event = objectMapper.readValue(rawEventData, UserDataUpdateEvent::class.java)
        logger().info("Futures order update event: $event")

        if (event.type == UserDataUpdateEventType.ORDER_TRADE_UPDATE) {
            val orderUpdatedEvent = OrderUpdatedEvent(
                event.order!!.id,
                parsePairWithoutDelimiter(event.order.symbol),
                OrderStatus.of(event.order.orderStatus)
            )


            //TODO: add try catch to not stop on error
            firstPrioritySubscriber?.accept(orderUpdatedEvent)
            orderUpdatedSubscribers.forEach { it.accept(orderUpdatedEvent) }
        }
    }

    private fun openConnectionAndScheduleReopen() {
        Thread {
            while (!Thread.interrupted()) {
                val connectionToClose = currentConnection
                val listenKey = httpClient.getListenKey()
                currentConnection = webSocket.openNewUserDataConnection(listenKey, this::onMessage)
                connectionToClose?.close()
                Thread.sleep(72000000)
            }
        }
            .apply { isDaemon = true }
            .start()
    }

    private fun scheduleTokenRefresh() {
        Thread {
            while (!Thread.interrupted()) {
                Thread.sleep(3000000)
                httpClient.refreshCurrentListenKey()
            }
        }
            .apply { isDaemon = true }
            .start()
    }

    enum class Priority {
        FIRST,
        ANY
    }
}