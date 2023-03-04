package cryptocatbox.exchange.binance.client.ws


data class BinanceWebSocketConnectionRequestMessage(
    val method: Method,
    val params: Array<String>,
) {
    enum class Method {
        SUBSCRIBE,
        UNSUBSCRIBE
    }

    override fun toString(): String {
        return "{\"method\": \"$method\", \"params\": [${params.joinToString("\", \"", "\"", "\"")}]}"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BinanceWebSocketConnectionRequestMessage

        if (method != other.method) return false
        if (!params.contentEquals(other.params)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = method.hashCode()
        result = 31 * result + params.contentHashCode()
        return result
    }
}

