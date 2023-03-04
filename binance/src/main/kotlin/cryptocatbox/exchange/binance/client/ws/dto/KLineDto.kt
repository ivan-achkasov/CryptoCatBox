package cryptocatbox.exchange.binance.client.ws.dto

import com.fasterxml.jackson.annotation.JsonAlias
import cryptocatbox.exchange.binance.client.ws.dto.KLineDataDto

data class KLineDto(
    @JsonAlias("E")
    val eventTime: Long,

    @JsonAlias("s", "ps")
    val symbol: String,

    @JsonAlias("k")
    val data: KLineDataDto
)
