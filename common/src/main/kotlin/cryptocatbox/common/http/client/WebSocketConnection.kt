package cryptocatbox.common.http.client

interface WebSocketConnection {
    fun connect()
    fun sendMessage(message: String, openIfMissed: Boolean = true)
}