package cryptocatbox.binance.infrastructure.persistence.entity.strategy

import cryptocatbox.common.domain.Currency
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal

@Table(schema = "binance", name = "grid_strategy")
data class GridStrategyDbEntity(
    @Id
    val id: Long,
    val startPrice: BigDecimal,
    val baseAsset: Currency,
    val quoteAsset: Currency,
    val stepDistinction: BigDecimal,
    val sellOrderQuantity: BigDecimal,
    val buyOrderQuantity: BigDecimal
)