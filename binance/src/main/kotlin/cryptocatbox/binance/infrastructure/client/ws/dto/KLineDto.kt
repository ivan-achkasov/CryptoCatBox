package cryptocatbox.binance.infrastructure.client.ws.dto

import com.fasterxml.jackson.annotation.JsonAlias

data class KLineDto(
    @JsonAlias("E")
    val eventTime: Long,

    @JsonAlias("s", "ps")
    val symbol: String,

    @JsonAlias("k")
    val data: KLineDataDto
)
