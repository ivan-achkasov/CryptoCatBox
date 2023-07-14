package cryptocatbox.binance.infrastructure.persistence.repository.strategy

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles

@DataJdbcTest
@ActiveProfiles("test")
open class GridStrategyOrderRepositoryTest {

    @Autowired
    private lateinit var gridStrategyOrderRepository: GridStrategyOrderRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Test
    fun tmpTest() {
        jdbcTemplate.execute("INSERT INTO binance.grid_strategy(id, start_price, base_asset, quote_asset, step_distinction, sell_order_quantity, buy_order_quantity) VALUES (1, 2.0, 'BNB', 'USDT', 22.0, 44.0, 55.0)")
        jdbcTemplate.execute("INSERT INTO binance.futures_order(id, exchange_id, base_asset, quote_asset, price, quantity, side, status) VALUES (4, 2, 'BNB', 'USDT', 22.0, 44.0, 'BUY', 'NEW')")
        //jdbcTemplate.execute("INSERT INTO binance.futures_order_to_strategy(strategy_id, order_id) VALUES (1, 4)")

        gridStrategyOrderRepository.associateOrderWithStrategy(1, 4)

        assertNotNull(gridStrategyOrderRepository.findByExchangeId(2))
    }
}