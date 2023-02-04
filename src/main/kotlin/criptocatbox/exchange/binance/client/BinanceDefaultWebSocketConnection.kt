package criptocatbox.exchange.binance.client

import criptocatbox.exchange.binance.client.BinanceWebSocketConnectionRequestMessage.Method.SUBSCRIBE
import criptocatbox.exchange.binance.client.BinanceWebSocketConnectionRequestMessage.Method.UNSUBSCRIBE
import criptocatbox.http.client.WebSocketCallback
import criptocatbox.http.client.WebSocketConnectionImpl

open class BinanceDefaultWebSocketConnection(
    private val onMessageCallback: WebSocketCallback,
    private val onOpenCallback: WebSocketCallback,
    private val onClosingCallback: WebSocketCallback,
    private val onFailureCallback: WebSocketCallback
) : WebSocketConnectionImpl("wss://fstream.binance.com/ws/", onMessageCallback, onOpenCallback, onClosingCallback, onFailureCallback) {

    private val connectionLimits = 200//TODO: Configurable

    private val activeStreams = HashSet<String>(connectionLimits)

    fun subscribe(streamName: String) {
        sendMessage(BinanceWebSocketConnectionRequestMessage(SUBSCRIBE, arrayOf(streamName)))
    }

    fun subscribe(streamNames: Collection<String>) {
        sendMessage(BinanceWebSocketConnectionRequestMessage(SUBSCRIBE, streamNames.toTypedArray()))
    }

    fun unsubscribe(streamName: String) {
        sendMessage(BinanceWebSocketConnectionRequestMessage(UNSUBSCRIBE, arrayOf(streamName)))
    }

    fun sendMessage(message: BinanceWebSocketConnectionRequestMessage) {
        //TODO: synchronized
        if(message.method == SUBSCRIBE) {
            activeStreams.addAll(message.params)
            if(activeStreams.size > connectionLimits) {
                throw RuntimeException("Too many streams per connection")
            }
        } else if (message.method == UNSUBSCRIBE) {
            activeStreams.removeAll(message.params.toSet())
        }
        if(message.params.size > 100) {
            val paramsChunk1 = message.params.sliceArray(IntRange(0, 99))
            super.sendMessage(BinanceWebSocketConnectionRequestMessage(message.method, paramsChunk1).toString(), true)
            val paramsChunk2 = message.params.sliceArray(IntRange(100, message.params.size - 1))
            super.sendMessage(BinanceWebSocketConnectionRequestMessage(message.method, paramsChunk2).toString(), true)
        } else {
            super.sendMessage(message.toString(), true)
        }
    }
}