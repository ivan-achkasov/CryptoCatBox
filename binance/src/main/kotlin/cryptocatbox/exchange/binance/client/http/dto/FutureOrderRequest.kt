package cryptocatbox.exchange.binance.client.http.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import cryptocatbox.exchange.binance.client.dto.OrderType
import cryptocatbox.exchange.binance.client.dto.PositionSide
import cryptocatbox.exchange.binance.client.dto.Side
import cryptocatbox.exchange.binance.client.dto.TimeInForce
import java.math.BigDecimal

@JsonInclude(Include.NON_NULL)
data class FutureOrderRequest(
    val symbol: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    val side: Side,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    val quantity: BigDecimal?,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    val price: BigDecimal?,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    val type: OrderType,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    val stopPrice: BigDecimal? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    val reduceOnly: Boolean? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    val positionSide: PositionSide? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    val priceProtect: Boolean? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    val closePosition: Boolean = false,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    val timeInForce: TimeInForce = TimeInForce.GTC
)