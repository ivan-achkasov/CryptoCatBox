package criptocatbox.exchange.binance.client.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include

@JsonInclude(Include.NON_NULL)
data class FutureOrderRequestDto(
    val symbol: String,
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    val side: Side,
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    val quantity: Double?,
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    val type: OrderType,
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    val stopPrice: Double? = null,
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    val reduceOnly: Boolean? = null,
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    val positionSide: PositionSide? = null,
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    val priceProtect: Boolean? = null,
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    val closePosition: Boolean = false
)