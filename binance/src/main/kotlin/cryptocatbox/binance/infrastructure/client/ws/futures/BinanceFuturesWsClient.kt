package cryptocatbox.binance.infrastructure.client.ws.futures

import cryptocatbox.binance.infrastructure.client.ws.config.FuturesWsClientProperties
import cryptocatbox.common.http.client.ReadOnlyWebSocketConnection
import cryptocatbox.common.http.client.WebSocketCallback
import org.springframework.stereotype.Component

@Component
class BinanceFuturesWsClient(private val properties: FuturesWsClientProperties) {

    fun openUserDataConnection(listenKey: String, onMessage: WebSocketCallback): ReadOnlyWebSocketConnection {
        val url = "${properties.baseUrl}/ws/$listenKey"
        return ReadOnlyWebSocketConnection(url, onMessage).apply { connect() }
    }

}