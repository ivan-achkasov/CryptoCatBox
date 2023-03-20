package cryptocatbox.binance.core.service.order

import cryptocatbox.binance.core.model.FuturesLimitOrder
import cryptocatbox.binance.core.model.LimitOrderQuery
import cryptocatbox.binance.core.model.OrderSide
import cryptocatbox.binance.core.model.OrderStatus
import cryptocatbox.binance.core.model.of
import cryptocatbox.binance.infrastructure.client.dto.OrderType
import cryptocatbox.binance.infrastructure.client.dto.Side
import cryptocatbox.binance.infrastructure.client.dto.of
import cryptocatbox.binance.infrastructure.client.http.FuturesHttpClient
import cryptocatbox.binance.infrastructure.client.http.dto.CancelOrderByOrderIdRequest
import cryptocatbox.binance.infrastructure.client.http.dto.ExchangeInfoResponse
import cryptocatbox.binance.infrastructure.client.http.dto.FutureOrderRequest
import cryptocatbox.binance.infrastructure.client.http.dto.PremiumIndexRequest
import cryptocatbox.binance.infrastructure.persistence.repository.FuturesOrderRepository
import cryptocatbox.common.Logging
import cryptocatbox.common.domain.CurrencyPair
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class BinanceFuturesService(
    private val futuresHttpClient: FuturesHttpClient,
    private val futuresOrderRepository: FuturesOrderRepository
) : InitializingBean, Logging {


    private lateinit var quantityAdjusters: Map<String, QuantityAdjuster>
    private lateinit var priceAdjusters: Map<String, PriceAdjuster>


    override fun afterPropertiesSet() {
        val exchangeInfoResponse = futuresHttpClient.getExchangeInfo()
        priceAdjusters =
            exchangeInfoResponse.symbols.associate { "${it.baseAsset}${it.marginAsset}" to it.getPriceAdjuster() }
        quantityAdjusters =
            exchangeInfoResponse.symbols.associate { "${it.baseAsset}${it.marginAsset}" to it.getQuantityAdjuster() }
    }

    private fun ExchangeInfoResponse.Symbol.getPriceAdjuster(): PriceAdjuster {
        val priceFilter = this.filters.find { it.filterType == ExchangeInfoResponse.FilterType.PRICE_FILTER }
        val precision = this.pricePrecision
        return PriceAdjuster(priceFilter!!.tickSize!!, precision)
    }

    private fun ExchangeInfoResponse.Symbol.getQuantityAdjuster(): QuantityAdjuster {
        val priceFilter = this.filters.find { it.filterType == ExchangeInfoResponse.FilterType.LOT_SIZE }
        val precision = this.quantityPrecision
        return QuantityAdjuster(priceFilter!!.stepSize!!, precision)
    }

    fun placeLimitOrder(orderQuery: LimitOrderQuery): FuturesLimitOrder {
        val symbol = "${orderQuery.symbol.buy}${orderQuery.symbol.sell}"
        val quantity = quantityAdjusters[symbol]!!.adjust(orderQuery.quantity)
        val price = priceAdjusters[symbol]!!.adjust(orderQuery.price)
        val response = futuresHttpClient.placeOrder(
            FutureOrderRequest(
                symbol,
                Side.of(orderQuery.side),
                quantity,
                price,
                OrderType.LIMIT,
                reduceOnly = orderQuery.reduceOnly
            )
        )

        //TODO: read what to use BigInteger or Long
        val order = FuturesLimitOrder(
            0,
            response.orderId.toLong(),
            orderQuery.symbol,
            response.price,
            orderQuery.quantity,
            OrderSide.of(response.side),
            OrderStatus.of(response.status)
        )

        return futuresOrderRepository.save(order)
    }

    fun getMarkPrice(symbolPair: CurrencyPair): BigDecimal {
        val symbol = "${symbolPair.buy}${symbolPair.sell}"
        return futuresHttpClient.getPremiumIndex(PremiumIndexRequest(symbol)).markPrice
    }

    fun cancelOrder(symbolPair: CurrencyPair, exchangeId: Long) {
        val symbol = "${symbolPair.buy}${symbolPair.sell}"
        futuresHttpClient.cancelOrder(CancelOrderByOrderIdRequest(symbol, exchangeId))
    }

    private data class PriceAdjuster(
        val tickSize: Double,
        val precision: Int
    ) {
        fun adjust(price: BigDecimal): BigDecimal {
            return round(price).setScale(precision, RoundingMode.UNNECESSARY)
        }

        fun round(price: BigDecimal): BigDecimal {
            return price - (price % tickSize.toBigDecimal())
        }
    }

    private data class QuantityAdjuster(
        val stepSize: Double,
        val precision: Int
    ) {
        fun adjust(quantity: BigDecimal): BigDecimal {
            return round(quantity).setScale(precision, RoundingMode.UNNECESSARY)
        }

        fun round(quantity: BigDecimal): BigDecimal {
            return quantity - (quantity % stepSize.toBigDecimal())
        }
    }
}