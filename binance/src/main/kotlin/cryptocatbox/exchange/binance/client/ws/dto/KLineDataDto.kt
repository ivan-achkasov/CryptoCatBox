package cryptocatbox.exchange.binance.client.ws.dto

import com.fasterxml.jackson.annotation.JsonAlias

data class KLineDataDto(
    @JsonAlias("t")
    val startTime: Long,

    @JsonAlias("T")
    val closeTime: Long,

    @JsonAlias("o")
    val openPrice: Double,

    @JsonAlias("c")
    val closePrice: Double,

    @JsonAlias("x")
    val closed: Boolean
)