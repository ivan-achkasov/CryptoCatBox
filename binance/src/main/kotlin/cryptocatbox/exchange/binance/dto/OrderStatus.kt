package cryptocatbox.exchange.binance.dto

import cryptocatbox.exchange.binance.client.dto.OrderStatus.*

enum class OrderStatus {
    NEW,
    PARTIALLY_FILLED,
    FILLED,
    CANCELED,
    EXPIRED;

    companion object
}

fun OrderStatus.Companion.of(status: cryptocatbox.exchange.binance.client.dto.OrderStatus): OrderStatus {
    return when (status) {
        NEW -> OrderStatus.NEW
        PARTIALLY_FILLED -> OrderStatus.PARTIALLY_FILLED
        FILLED -> OrderStatus.FILLED
        CANCELED -> OrderStatus.CANCELED
        EXPIRED -> OrderStatus.EXPIRED
    }

}