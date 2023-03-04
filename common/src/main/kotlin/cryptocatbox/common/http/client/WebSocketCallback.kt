package cryptocatbox.common.http.client

fun interface WebSocketCallback {

    companion object {
        val EMPTY = WebSocketCallback{}
    }

    fun onReceive(data: String)
}
