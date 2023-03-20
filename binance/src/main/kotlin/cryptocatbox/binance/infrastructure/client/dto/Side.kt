package cryptocatbox.binance.infrastructure.client.dto

import cryptocatbox.binance.core.model.OrderSide
import cryptocatbox.binance.core.model.OrderSide.BUY
import cryptocatbox.binance.core.model.OrderSide.SELL

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
