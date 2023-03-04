package cryptocatbox.exchange.binance.client.ws

import cryptocatbox.common.domain.CurrencyPair

class FuturesWebSocketMessages private constructor() {
    companion object {
        fun subscribePerpetual1MinCandles(pairs: Set<CurrencyPair>): BinanceWebSocketConnectionRequestMessage {
            val streams = pairs
                .map { pair -> "${pair.buy}${pair.sell}".lowercase() + "_perpetual@continuousKline_1m" }
                .toTypedArray()
            return BinanceWebSocketConnectionRequestMessage(
                BinanceWebSocketConnectionRequestMessage.Method.SUBSCRIBE,
                streams
            )
        }

        fun unsubscribePerpetual1MinCandles(pair: CurrencyPair): BinanceWebSocketConnectionRequestMessage {
            return BinanceWebSocketConnectionRequestMessage(
                BinanceWebSocketConnectionRequestMessage.Method.UNSUBSCRIBE,
                arrayOf("${pair.buy}${pair.sell}".lowercase() + "_perpetual@continuousKline_1m")
            )
        }
    }
}