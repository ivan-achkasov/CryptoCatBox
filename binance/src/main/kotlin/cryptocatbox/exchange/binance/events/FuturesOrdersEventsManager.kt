package cryptocatbox.exchange.binance.events

import com.fasterxml.jackson.databind.ObjectMapper
import cryptocatbox.common.Logging
import cryptocatbox.common.domain.parsePairWithoutDelimiter
import cryptocatbox.common.logger
import cryptocatbox.exchange.binance.client.http.FuturesHttpClient
import cryptocatbox.exchange.binance.client.ws.BinanceFuturesWebSocketClient
import cryptocatbox.exchange.binance.client.ws.dto.UserDataUpdateEvent
import cryptocatbox.exchange.binance.client.dto.UserDataUpdateEventType
import cryptocatbox.exchange.binance.dto.OrderStatus
import cryptocatbox.exchange.binance.dto.of
import cryptocatbox.exchange.binance.events.dto.OrderUpdatedEvent
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
    private val orderUpdatedSubscribers = Collections.synchronizedList(mutableListOf<Consumer<OrderUpdatedEvent>>())

    override fun afterPropertiesSet() {
        val listenKey = httpClient.getListenKey()
        webSocket.openNewUserDataConnection(listenKey, this::onMessage)
        scheduleTokenRefresh()
    }

    fun subscribe(eventHandleMethod: Consumer<OrderUpdatedEvent>) {
        orderUpdatedSubscribers.add(eventHandleMethod)
    }

    private fun onMessage(rawEventData: String) {
        val event = objectMapper.readValue(rawEventData, UserDataUpdateEvent::class.java)
        logger().info("Futures order update event: $event")

        if (event.type == UserDataUpdateEventType.ORDER_TRADE_UPDATE) {
            orderUpdatedSubscribers.forEach {
                val orderUpdatedEvent = OrderUpdatedEvent(
                    event.order!!.id,
                    parsePairWithoutDelimiter(event.order.symbol),
                    OrderStatus.of(event.order.orderStatus)
                )
                it.accept(orderUpdatedEvent)
            }
        }
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
}