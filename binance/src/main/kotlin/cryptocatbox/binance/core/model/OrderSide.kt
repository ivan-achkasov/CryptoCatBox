package cryptocatbox.binance.core.model

import cryptocatbox.binance.infrastructure.client.dto.Side
import cryptocatbox.binance.infrastructure.client.dto.Side.BUY
import cryptocatbox.binance.infrastructure.client.dto.Side.SELL

enum class OrderSide {
    BUY,
    SELL;

    companion object
}

fun OrderSide.Companion.of(orderSide: Side): OrderSide {
    return when (orderSide) {
        BUY -> OrderSide.BUY
        SELL -> OrderSide.SELL
    }
}