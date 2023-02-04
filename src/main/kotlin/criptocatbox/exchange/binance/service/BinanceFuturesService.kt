package criptocatbox.exchange.binance.service

import criptocatbox.Logging
import criptocatbox.domain.CurrencyPair
import criptocatbox.exchange.binance.client.BinanceFuturesHttpClient
import criptocatbox.exchange.binance.client.dto.FutureOrderRequestDto
import criptocatbox.exchange.binance.client.dto.OrderType
import criptocatbox.exchange.binance.client.dto.Side
import criptocatbox.logger
import org.springframework.stereotype.Service

@Service
class BinanceFuturesService(private val futuresHttpClient: BinanceFuturesHttpClient): Logging {

    fun buyWithMarketPrice(pair: CurrencyPair, quantity: Double, takeProfitPrice: Double, stopLossPrice: Double) {
        logger().info("Buy order $pair, quantity: $quantity, takeProfitPrice: $takeProfitPrice, stopLossPrice: $stopLossPrice")
        val symbol = "${pair.buy}${pair.sell}"
        futuresHttpClient.placeOrders(listOf(
            FutureOrderRequestDto(symbol, Side.BUY, quantity, OrderType.MARKET),
            FutureOrderRequestDto(symbol, Side.SELL, null, OrderType.TAKE_PROFIT_MARKET, takeProfitPrice, closePosition = true),
            FutureOrderRequestDto(symbol, Side.SELL, null, OrderType.STOP_MARKET, stopLossPrice, closePosition = true)
        ))
    }

    fun sellWithMarketPrice(pair: CurrencyPair, quantity: Double, takeProfitPrice: Double, stopLossPrice: Double) {
        logger().info("Sell order $pair, quantity: $quantity, takeProfitPrice: $takeProfitPrice, stopLossPrice: $stopLossPrice")
        val symbol = "${pair.buy}${pair.sell}"
        futuresHttpClient.placeOrders(listOf(
            FutureOrderRequestDto(symbol, Side.SELL, quantity, OrderType.MARKET),
            FutureOrderRequestDto(symbol, Side.BUY, null, OrderType.TAKE_PROFIT_MARKET, takeProfitPrice, closePosition = true),
            FutureOrderRequestDto(symbol, Side.BUY, null, OrderType.STOP_MARKET, stopLossPrice, closePosition = true)
        ))
    }
}