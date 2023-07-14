package cryptocatbox.binance.infrastructure.persistence.repository

import cryptocatbox.binance.core.model.FuturesLimitOrder
import cryptocatbox.binance.core.model.OrderSide
import cryptocatbox.binance.core.model.OrderStatus
import cryptocatbox.common.domain.Currency
import cryptocatbox.common.domain.CurrencyPair
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.test.context.ActiveProfiles

@DataJdbcTest
@ActiveProfiles("test")
open class FuturesOrderRepositoryImplTest {

    @Autowired
    private lateinit var futuresOrderRepository: FuturesOrderRepositoryImpl

    @Test
    fun tmpTest() {
        val order = FuturesLimitOrder(
            0,
            2,
            CurrencyPair(Currency.BNB, Currency.USDT),
            23.0.toBigDecimal(),
            45.5.toBigDecimal(),
            OrderSide.BUY,
            OrderStatus.FILLED
        )
        futuresOrderRepository.save(order)
        println(futuresOrderRepository.findByExchangeId(2))
    }
}