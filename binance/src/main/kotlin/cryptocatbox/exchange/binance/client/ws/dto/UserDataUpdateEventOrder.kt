package cryptocatbox.exchange.binance.client.ws.dto

import com.fasterxml.jackson.annotation.JsonAlias
import cryptocatbox.exchange.binance.client.dto.OrderStatus

data class UserDataUpdateEventOrder(
    @JsonAlias("i") val id: Long,
    @JsonAlias("s") val symbol: String,
    @JsonAlias("X") val orderStatus: OrderStatus
)
