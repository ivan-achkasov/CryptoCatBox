package cryptocatbox.common.http.client

open class WebSocketConnectionImpl(
    url: String,
    onMessageCallback: WebSocketCallback,
    onOpenCallback: EmptyWebSocketCallback?,
    onClosingCallback: EmptyWebSocketCallback?,
    onFailureCallback: EmptyWebSocketCallback?
) : ReadOnlyWebSocketConnection(url, onMessageCallback, onOpenCallback, onClosingCallback, onFailureCallback),
    WebSocketConnection {

    private val mutex: Any = Any()

    override fun sendMessage(message: String, openIfMissed: Boolean) {
        synchronized(mutex) {
            if (getWebSocket() == null) {
                if (openIfMissed) {
                    connect()
                } else {
                    throw ConnectionNotOpenException("Connection[$connectionId}] is not open yet.")
                }
            }
            getWebSocket()!!.send(message)
        }
    }
}