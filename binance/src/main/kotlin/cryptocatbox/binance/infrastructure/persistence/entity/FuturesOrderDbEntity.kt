package cryptocatbox.binance.infrastructure.persistence.entity

import cryptocatbox.binance.core.model.OrderSide
import cryptocatbox.binance.core.model.OrderStatus
import cryptocatbox.common.domain.Currency
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal

@Table(schema = "binance", name = "futures_order")
data class FuturesOrderDbEntity(
    @Id
    val id: Long,
    val exchangeId: Long,
    val baseAsset: Currency,
    val quoteAsset: Currency,
    val price: BigDecimal,
    val quantity: BigDecimal,
    val side: OrderSide,
    val status: OrderStatus
)