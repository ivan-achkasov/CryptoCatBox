package cryptocatbox.binance.infrastructure.client.ws

import cryptocatbox.common.http.client.WebSocketCallback
import cryptocatbox.common.http.client.WebSocketConnectionImpl

open class UserDataStreamsConnection(
    listenKey: String,
    onMessageCallback: WebSocketCallback
) : WebSocketConnectionImpl("wss://fstream.binance.com/ws/${listenKey}", onMessageCallback) {

}