package cryptocatbox.binance.infrastructure.persistence.repository.dao

import cryptocatbox.binance.core.model.OrderSide
import cryptocatbox.binance.core.model.OrderStatus
import cryptocatbox.binance.infrastructure.persistence.dao.FuturesOrderDao
import cryptocatbox.binance.infrastructure.persistence.entity.FuturesOrderDbEntity
import cryptocatbox.common.domain.Currency
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.test.context.ActiveProfiles

@DataJdbcTest
@ActiveProfiles("test")
open class FuturesOrderDaoTest {

    @Autowired
    private lateinit var futuresOrderDao: FuturesOrderDao

    @Test
    fun tmp() {
        val order = FuturesOrderDbEntity(
            0,
            2,
            Currency.BNB,
            Currency.USDT,
            23.0.toBigDecimal(),
            45.5.toBigDecimal(),
            OrderSide.BUY,
            OrderStatus.FILLED
        )
        println(futuresOrderDao.save(order))
        println(futuresOrderDao.findByExchangeId(2))
    }
}