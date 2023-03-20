package cryptocatbox.binance.infrastructure.persistence.rowmapper

import cryptocatbox.binance.core.model.FuturesLimitOrder
import cryptocatbox.binance.core.model.OrderSide
import cryptocatbox.binance.core.model.OrderStatus
import cryptocatbox.common.domain.Currency
import cryptocatbox.common.domain.CurrencyPair
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class FuturesLimitOrderRowMapper(private val prefix: String = "") : RowMapper<FuturesLimitOrder> {

    override fun mapRow(rs: ResultSet, rowNum: Int): FuturesLimitOrder {
        return FuturesLimitOrder(
            rs.getLong("${prefix}id"),
            rs.getLong("${prefix}exchange_id"),
            CurrencyPair(
                Currency.valueOf(rs.getString("${prefix}base_asset")),
                Currency.valueOf(rs.getString("${prefix}quote_asset"))
            ),
            rs.getBigDecimal("${prefix}price"),
            rs.getBigDecimal("${prefix}quantity"),
            OrderSide.valueOf(rs.getString("${prefix}side")),
            OrderStatus.valueOf(rs.getString("${prefix}status"))
        )
    }

}