package cryptocatbox.common.http.client

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.Random

open class ReadOnlyWebSocketConnection(
    private val url: String,
    private val onMessageCallback: WebSocketCallback,
    private val onOpenCallback: EmptyWebSocketCallback? = null,
    private val onClosingCallback: EmptyWebSocketCallback? = null,
    private val onFailureCallback: EmptyWebSocketCallback? = null
) : WebSocketListener() {
    private val logger: Logger = LoggerFactory.getLogger(ReadOnlyWebSocketConnection::class.java)

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }

    protected val connectionId: UInt = Random().nextInt().toUInt()

    private val client: OkHttpClient = OkHttpClientSingleton.HTTP_CLIENT

    private var webSocket: WebSocket? = null

    private val mutex: Any = Any()

    fun connect() {
        synchronized(mutex) {
            if (null == webSocket) {
                logger.info("Connection[{}] Connecting to {}", connectionId, url)
                val request = Request.Builder().url(url).get().build()
                webSocket = client.newWebSocket(request, this)
            } else {
                logger.info("Connection[{}] is already connected to {}", connectionId, url)
            }
        }
    }


    fun close() {
        if (webSocket != null) {
            logger.info("Connection[{}] Closing connection to {}", connectionId, url)
            webSocket!!.close(NORMAL_CLOSURE_STATUS, null)
        }
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        onMessageCallback.onReceive(text)
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        logger.info("WS Connection[{}] Connected to Server", connectionId)
        onOpenCallback?.process()
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        logger.info("WS Connection[{}] closed with code: {} and reason: '{}'", connectionId, code, reason)
        onClosingCallback?.process()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        logger.error("WS Connection[{}] Failure", connectionId, t)
        onFailureCallback?.process()
    }

    protected fun getWebSocket(): WebSocket? {
        return webSocket
    }
}