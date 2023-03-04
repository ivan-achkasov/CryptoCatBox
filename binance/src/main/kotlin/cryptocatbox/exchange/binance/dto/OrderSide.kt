package cryptocatbox.exchange.binance.dto

import cryptocatbox.exchange.binance.client.dto.Side
import cryptocatbox.exchange.binance.client.dto.Side.BUY
import cryptocatbox.exchange.binance.client.dto.Side.SELL

enum class OrderSide {
    BUY,
    SELL;

    companion object
}

fun OrderSide.Companion.of(orderSide: Side): OrderSide{
    return when(orderSide) {
        BUY -> OrderSide.BUY
        SELL -> OrderSide.SELL
    }
}