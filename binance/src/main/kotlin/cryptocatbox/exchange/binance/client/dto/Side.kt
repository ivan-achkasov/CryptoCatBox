package cryptocatbox.exchange.binance.client.dto

import cryptocatbox.exchange.binance.dto.OrderSide
import cryptocatbox.exchange.binance.dto.OrderSide.BUY
import cryptocatbox.exchange.binance.dto.OrderSide.SELL

enum class Side {
    BUY,
    SELL;

    companion object
}

fun Side.Companion.of(orderSide: OrderSide): Side {
    return when(orderSide) {
        BUY -> Side.BUY
        SELL -> Side.SELL
    }
}
