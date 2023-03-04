package cryptocatbox.exchange.binance.service

import cryptocatbox.common.Logging
import cryptocatbox.exchange.binance.client.http.dto.ExchangeInfoResponse
import cryptocatbox.exchange.binance.client.dto.OrderType
import cryptocatbox.exchange.binance.client.http.FuturesHttpClient
import cryptocatbox.exchange.binance.client.http.dto.FutureOrderRequest
import cryptocatbox.exchange.binance.client.dto.Side
import cryptocatbox.exchange.binance.client.dto.of
import cryptocatbox.exchange.binance.client.http.dto.BatchFuturesOrdersRequest
import cryptocatbox.exchange.binance.dto.*
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class BinanceFuturesService(private val futuresHttpClient: FuturesHttpClient) : InitializingBean, Logging {


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

    fun placeLimitOrders(ordersQueries: List<LimitOrderQuery>): List<LimitOrder> {
        val requests = ordersQueries.map { orderQuery ->
            val symbol = "${orderQuery.pair.buy}${orderQuery.pair.sell}"
            val quantity = quantityAdjusters[symbol]!!.adjust(orderQuery.quantity)
            val price = priceAdjusters[symbol]!!.adjust(orderQuery.price)
            FutureOrderRequest(
                symbol,
                Side.of(orderQuery.side),
                quantity,
                price,
                OrderType.LIMIT,
                reduceOnly = orderQuery.reduceOnly
            )
        }
        val response = futuresHttpClient.placeOrders(BatchFuturesOrdersRequest(requests))
        return response.map {
            LimitOrder(
                it.orderId.toLong(),
                OrderSide.of(it.side),
                OrderStatus.of(it.status),
                it.price
            )
        }
    }

    fun placeLimitOrder(orderQuery: LimitOrderQuery): LimitOrder {
        val symbol = "${orderQuery.pair.buy}${orderQuery.pair.sell}"
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
        return LimitOrder(
            response.orderId.toLong(),
            OrderSide.of(response.side),
            OrderStatus.of(response.status),
            response.price
        )
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