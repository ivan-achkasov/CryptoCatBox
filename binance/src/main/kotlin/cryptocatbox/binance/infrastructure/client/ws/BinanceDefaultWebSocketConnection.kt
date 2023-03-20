package cryptocatbox.binance.infrastructure.client.ws

import cryptocatbox.binance.infrastructure.client.ws.BinanceWebSocketConnectionRequestMessage.Method.SUBSCRIBE
import cryptocatbox.binance.infrastructure.client.ws.BinanceWebSocketConnectionRequestMessage.Method.UNSUBSCRIBE
import cryptocatbox.common.http.client.WebSocketCallback
import cryptocatbox.common.http.client.WebSocketConnectionImpl

open class BinanceDefaultWebSocketConnection(
    private val onMessageCallback: WebSocketCallback,
    private val onOpenCallback: WebSocketCallback,
    private val onClosingCallback: WebSocketCallback,
    private val onFailureCallback: WebSocketCallback
) : WebSocketConnectionImpl("wss://fstream.binance.com/ws/", onMessageCallback, onOpenCallback, onClosingCallback, onFailureCallback) {

    private val streamsLimits = 200//TODO: Configurable

    private val activeStreams = HashSet<String>(streamsLimits)

    fun subscribe(streamName: String) {
        sendMessage(
            BinanceWebSocketConnectionRequestMessage(
                SUBSCRIBE,
                arrayOf(streamName)
            )
        )
    }

    fun subscribe(streamNames: Collection<String>) {
        sendMessage(
            BinanceWebSocketConnectionRequestMessage(
                SUBSCRIBE,
                streamNames.toTypedArray()
            )
        )
    }

    fun unsubscribe(streamName: String) {
        sendMessage(
            BinanceWebSocketConnectionRequestMessage(
                UNSUBSCRIBE,
                arrayOf(streamName)
            )
        )
    }

    fun sendMessage(message: BinanceWebSocketConnectionRequestMessage) {
        //TODO: synchronized
        if(message.method == SUBSCRIBE) {
            activeStreams.addAll(message.params)
            if(activeStreams.size > streamsLimits) {
                throw RuntimeException("Too many streams per connection")
            }
        } else if (message.method == UNSUBSCRIBE) {
            activeStreams.removeAll(message.params.toSet())
        }
        if(message.params.size > 100) {
            val paramsChunk1 = message.params.sliceArray(IntRange(0, 99))
            super.sendMessage(
                BinanceWebSocketConnectionRequestMessage(
                    message.method,
                    paramsChunk1
                ).toString(), true)
            val paramsChunk2 = message.params.sliceArray(IntRange(100, message.params.size - 1))
            super.sendMessage(
                BinanceWebSocketConnectionRequestMessage(
                    message.method,
                    paramsChunk2
                ).toString(), true)
        } else {
            super.sendMessage(message.toString(), true)
        }
    }
}