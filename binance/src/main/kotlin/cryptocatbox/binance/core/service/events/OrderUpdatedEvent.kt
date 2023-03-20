package cryptocatbox.binance.core.service.events

import cryptocatbox.binance.core.model.OrderStatus
import cryptocatbox.common.domain.CurrencyPair


data class OrderUpdatedEvent(
    val orderId: Long,
    val pair: CurrencyPair,
    val status: OrderStatus
)
