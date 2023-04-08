package cryptocatbox.binance.infrastructure.client.ws.dto

import com.fasterxml.jackson.annotation.JsonAlias
import cryptocatbox.binance.infrastructure.client.dto.UserDataUpdateEventType

data class UserDataUpdateEvent(
    @JsonAlias("e") val type: UserDataUpdateEventType,
    @JsonAlias("E") val timestamp: Long,
    @JsonAlias("o") val order: UserDataUpdateEventOrder?
)
