package criptocatbox.http.client

interface WebSocketConnection {
    fun connect()
    fun sendMessage(message: String, openIfMissed: Boolean = true)
}