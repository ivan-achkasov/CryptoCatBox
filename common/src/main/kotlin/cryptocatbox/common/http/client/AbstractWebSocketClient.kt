package cryptocatbox.common.http.client

import cryptocatbox.common.Logging
import cryptocatbox.common.logger
import java.util.concurrent.ConcurrentHashMap

abstract class AbstractWebSocketClient<C: WebSocketConnectionImpl>: Logging {

    companion object {
        val EMPTY_CALLBACK = WebSocketCallback {}
    }

    private val connections = ConcurrentHashMap<Int, C>()

    fun closeConnection(connectionId: Int) {
        if (connections.containsKey(connectionId)) {
            connections[connectionId]!!.close()
            logger().info("Closing Connection[{}]", connectionId)
            connections.remove(connectionId)
        } else {
            logger().info("Connection[{}] does not exist!", connectionId)
        }
    }

    fun closeAllConnections() {
        logger().info("Closing {} connection(s)", connections.size)
        val iterator = connections.entries.iterator()
        while (iterator.hasNext()) {
            val connection = iterator.next().value
            connection.close()
            iterator.remove()
        }
    }

    protected fun registerConnection(connection: C): Int {
        val connectionId = connection.connectionId
        if(connections.contains(connectionId)) {
            throw RuntimeException("Connection with this id already exists")
        }
        connection.connect()
        return connectionId
    }
}