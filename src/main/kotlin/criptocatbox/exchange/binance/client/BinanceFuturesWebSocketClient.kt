package criptocatbox.exchange.binance.client

import criptocatbox.http.client.AbstractWebSocketClient
import criptocatbox.http.client.WebSocketCallback
import org.springframework.stereotype.Component

@Component
class BinanceFuturesWebSocketClient: AbstractWebSocketClient<BinanceDefaultWebSocketConnection>() {

    fun openNewDefaultConnection(onMessage: WebSocketCallback): BinanceDefaultWebSocketConnection {
        val connection = BinanceDefaultWebSocketConnection(onMessage, WebSocketCallback.EMPTY, WebSocketCallback.EMPTY, WebSocketCallback.EMPTY)
        super.registerConnection(connection)
        return connection
    }
}