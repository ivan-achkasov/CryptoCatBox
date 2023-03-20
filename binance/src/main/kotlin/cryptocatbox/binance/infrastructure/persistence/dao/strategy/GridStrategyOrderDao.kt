package cryptocatbox.binance.infrastructure.persistence.dao.strategy

import cryptocatbox.binance.infrastructure.persistence.entity.strategy.GridStrategyOrderDbEntity
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.Repository
import org.springframework.data.repository.query.Param

interface GridStrategyOrderDao : Repository<GridStrategyOrderDbEntity, Long> {
    @Modifying
    @Query("INSERT INTO binance.futures_order_to_strategy(strategy_id, order_id) VALUES (:strategyId, :orderId)")
    fun associateOrderWithStrategy(@Param("strategyId") strategyId: Long, @Param("orderId") orderId: Long)

    @Query(
        value = """
            SELECT ots.strategy_id as strategy_id,
                o.id as order_id,
                o.exchange_id as order_exchange_id,
                o.base_asset as order_base_asset,
                o.quote_asset as order_quote_asset,
                o.price as order_price,
                o.quantity as order_quantity,
                o.side as order_side,
                o.status as order_status
            FROM binance.futures_order_to_strategy ots JOIN binance.futures_order o ON o.id = ots.order_id
            WHERE ots.strategy_id = :strategyId AND o.side = 'SELL' AND o.status NOT IN ('FILLED', 'CANCELED', 'EXPIRED')
            ORDER BY o.price
            FETCH FIRST 1 ROWS ONLY
        """
    )
    fun findLowerOpenSellOrderForStrategy(@Param("strategyId") strategyId: Long): GridStrategyOrderDbEntity?

    @Query(
        value = """
            SELECT ots.strategy_id as strategy_id,
                o.id as order_id,
                o.exchange_id as order_exchange_id,
                o.base_asset as order_base_asset,
                o.quote_asset as order_quote_asset,
                o.price as order_price,
                o.quantity as order_quantity,
                o.side as order_side,
                o.status as order_status
            FROM binance.futures_order_to_strategy ots JOIN binance.futures_order o ON o.id = ots.order_id
            WHERE ots.strategy_id = :strategyId AND o.side = 'BUY' AND o.status NOT IN ('FILLED', 'CANCELED', 'EXPIRED')
            ORDER BY o.price
            FETCH FIRST 1 ROWS ONLY
        """
    )
    fun findLowerOpenBuyOrderForStrategy(@Param("strategyId") strategyId: Long): GridStrategyOrderDbEntity?

    @Query(
        value = """
            SELECT ots.strategy_id as strategy_id,
                o.id as order_id,
                o.exchange_id as order_exchange_id,
                o.base_asset as order_base_asset,
                o.quote_asset as order_quote_asset,
                o.price as order_price,
                o.quantity as order_quantity,
                o.side as order_side,
                o.status as order_status
            FROM binance.futures_order_to_strategy ots JOIN binance.futures_order o ON o.id = ots.order_id
            WHERE ots.strategy_id = :strategyId AND o.side = 'BUY' AND o.status NOT IN ('FILLED', 'CANCELED', 'EXPIRED')
            ORDER BY o.price DESC
            FETCH FIRST 1 ROWS ONLY
        """
    )
    fun findHigherOpenBuyOrderForStrategy(@Param("strategyId") strategyId: Long): GridStrategyOrderDbEntity?

    @Query(
        value = """
            SELECT ots.strategy_id as strategy_id,
                o.id as order_id,
                o.exchange_id as order_exchange_id,
                o.base_asset as order_base_asset,
                o.quote_asset as order_quote_asset,
                o.price as order_price,
                o.quantity as order_quantity,
                o.side as order_side,
                o.status as order_status
            FROM binance.futures_order_to_strategy ots JOIN binance.futures_order o ON o.id = ots.order_id
            WHERE ots.strategy_id = :strategyId AND o.side = 'SELL' AND o.status NOT IN ('FILLED', 'CANCELED', 'EXPIRED')
            ORDER BY o.price DESC
            FETCH FIRST 1 ROWS ONLY
        """
    )
    fun findHigherOpenSellOrderForStrategy(@Param("strategyId") strategyId: Long): GridStrategyOrderDbEntity?

    @Query(
        value = """
            SELECT ots.strategy_id as strategy_id,
                o.id as order_id,
                o.exchange_id as order_exchange_id,
                o.base_asset as order_base_asset,
                o.quote_asset as order_quote_asset,
                o.price as order_price,
                o.quantity as order_quantity,
                o.side as order_side,
                o.status as order_status
            FROM binance.futures_order_to_strategy ots JOIN binance.futures_order o ON ots.order_id = o.id
            WHERE o.exchange_id = :exchangeId
        """
    )
    fun findByExchangeId(@Param("exchangeId") exchangeId: Long): GridStrategyOrderDbEntity?
}