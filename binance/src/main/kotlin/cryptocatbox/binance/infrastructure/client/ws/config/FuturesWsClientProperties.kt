package cryptocatbox.binance.infrastructure.client.ws.config

import org.springframework.stereotype.Component

@Component
class FuturesWsClientProperties(
    val baseUrl: String = "wss://fstream.binance.com"
)