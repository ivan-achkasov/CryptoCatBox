package cryptocatbox.exchange.binance.client.ws.dto

import com.fasterxml.jackson.annotation.JsonAlias
import cryptocatbox.exchange.binance.client.dto.UserDataUpdateEventType

data class UserDataUpdateEvent(
    @JsonAlias("e") val type: UserDataUpdateEventType,
    @JsonAlias("o") val order: UserDataUpdateEventOrder?
)
