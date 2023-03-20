package cryptocatbox.binance.infrastructure.client.ws

import cryptocatbox.common.http.client.AbstractWebSocketClient
import cryptocatbox.common.http.client.WebSocketCallback
import cryptocatbox.common.http.client.WebSocketConnectionImpl
import org.springframework.stereotype.Component

@Component
class BinanceFuturesWebSocketClient: AbstractWebSocketClient<WebSocketConnectionImpl>() {

    fun openNewDefaultConnection(onMessage: WebSocketCallback): BinanceDefaultWebSocketConnection {
        val connection = BinanceDefaultWebSocketConnection(
            onMessage,
            WebSocketCallback.EMPTY,
            WebSocketCallback.EMPTY,
            WebSocketCallback.EMPTY
        )
        super.registerConnection(connection)
        return connection
    }

    //TODO:rework. This stream is read only and does not have messages to open/close connection
    fun openNewUserDataConnection(listenKey: String, onMessage: WebSocketCallback): UserDataStreamsConnection {
        val connection = UserDataStreamsConnection(listenKey, onMessage);
        super.registerConnection(connection)
        return connection
    }
}