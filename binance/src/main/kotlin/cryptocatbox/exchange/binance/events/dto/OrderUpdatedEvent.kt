package cryptocatbox.exchange.binance.events.dto

import cryptocatbox.common.domain.CurrencyPair
import cryptocatbox.exchange.binance.dto.OrderStatus


data class OrderUpdatedEvent(
    val orderId: Long,
    val pair: CurrencyPair,
    val status: OrderStatus
)
