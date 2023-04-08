package cryptocatbox.common.http.client

fun interface WebSocketCallback {
    fun onReceive(data: String)
}
