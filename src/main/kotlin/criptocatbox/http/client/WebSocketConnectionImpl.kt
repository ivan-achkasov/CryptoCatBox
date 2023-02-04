package criptocatbox.http.client

import criptocatbox.Logging
import criptocatbox.logger
import okhttp3.*
import java.util.concurrent.atomic.AtomicInteger

open class WebSocketConnectionImpl(
    private val url: String,
    private val onMessageCallback: WebSocketCallback,
    private val onOpenCallback: WebSocketCallback = WebSocketCallback.EMPTY,
    private val onClosingCallback: WebSocketCallback = WebSocketCallback.EMPTY,
    private val onFailureCallback: WebSocketCallback = WebSocketCallback.EMPTY
) : WebSocketListener(), WebSocketConnection, Logging {

    companion object {
        private val CONNECTION_COUNTER = AtomicInteger(0)
        private const val NORMAL_CLOSURE_STATUS = 1000
    }

    val connectionId = CONNECTION_COUNTER.incrementAndGet()

    private val client: OkHttpClient = OkHttpClientSingleton.HTTP_CLIENT

    private val streamName = url

    private var webSocket: WebSocket? = null

    private val mutex: Any = Any()


    override fun connect() {
        synchronized(mutex) {
            if (null == webSocket) {
                logger().info("Connection[{}] Connecting to {}", connectionId, streamName)
                val request = Request.Builder().url(url).get().build()
                webSocket = client.newWebSocket(request, this)
            } else {
                logger().info("Connection[{}] is already connected to {}", connectionId, streamName)
            }
        }
    }


    fun close() {
        if (null != webSocket) {
            logger().info("Connection[{}] Closing connection to {}", connectionId, streamName)
            webSocket!!.close(NORMAL_CLOSURE_STATUS, null)
        }
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        logger().info("Connection[{}] Connected to Server", connectionId)
        onOpenCallback.onReceive("")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        logger().info("Connection[{}] closed with code: {} and reason: '{}'", connectionId, code, reason)
        onClosingCallback.onReceive(reason)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        onMessageCallback.onReceive(text)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        logger().error("Connection[{}] Failure", connectionId, t)
        onFailureCallback.onReceive("")//TODO:??
    }

    override fun sendMessage(message: String, openIfMissed: Boolean) {
        synchronized(mutex) {
            if (webSocket == null) {
                if(openIfMissed) {
                    connect()
                } else {
                    throw ConnectionNotOpenException("Connection[$connectionId}] is not open yet.")
                }
            }
            webSocket!!.send(message)
        }
    }
}