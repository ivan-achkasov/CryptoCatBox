package cryptocatbox.binance.core.service.strategy.grid

import cryptocatbox.binance.core.model.FuturesLimitOrder
import cryptocatbox.binance.core.model.LimitOrderQuery
import cryptocatbox.binance.core.model.OrderSide
import cryptocatbox.binance.core.model.strategy.GridSettings
import cryptocatbox.binance.core.model.strategy.GridStrategy
import cryptocatbox.binance.core.model.strategy.GridStrategyOrder
import cryptocatbox.binance.core.model.strategy.GridStrategyUtils
import cryptocatbox.binance.core.service.order.BinanceFuturesService
import cryptocatbox.binance.infrastructure.persistence.repository.strategy.GridStrategyOrderRepository
import cryptocatbox.binance.infrastructure.persistence.repository.strategy.GridStrategyRepository
import cryptocatbox.common.core.BigDecimalUtils
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class GridStrategyServiceImpl(
    private val futuresService: BinanceFuturesService,
    private val strategyRepository: GridStrategyRepository,
    private val orderRepository: GridStrategyOrderRepository
) : GridStrategyService {

    companion object {
        private const val NUM_OF_ORDERS = 20
    }

    override fun startNew(settings: GridSettings) {
        val pairCurrentPrice = futuresService.getMarkPrice(settings.symbol)

        val strategy = strategyRepository.save(GridStrategy(0, pairCurrentPrice, settings))

        placeLeadOffBuyOrders(pairCurrentPrice, strategy)
        placeLeadOffSellOrders(pairCurrentPrice, strategy)
    }

    override fun processFilledOrder(gridOrder: GridStrategyOrder) {
        val strategy = strategyRepository.findById(gridOrder.strategyId)
            ?: throw RuntimeException("Grid strategy not found")

        when (gridOrder.order.side) {
            OrderSide.BUY -> {
                placeNewLowerSellOrderBasedOnFilled(gridOrder.order, strategy)
                placeNewLowerBuyOrder(strategy)
                cancelHigherSellOrder(strategy.id)
            }

            OrderSide.SELL -> {
                placeNewHigherBuyOrderBasedOnFilled(gridOrder.order, strategy)
                placeNewHigherSellOrder(strategy)
                cancelLowerBuyOrder(strategy.id)
            }
        }
    }

    private fun placeNewLowerSellOrderBasedOnFilled(filledOrder: FuturesLimitOrder, strategy: GridStrategy) {
        val lowerSellOrder = orderRepository.findLowerOpenSellOrderForStrategy(strategy.id)
        val sellPrice = BigDecimalUtils.avg(filledOrder.price, lowerSellOrder!!.order.price)
        placeOrder(sellPrice, OrderSide.SELL, strategy)
    }

    private fun placeNewLowerBuyOrder(strategy: GridStrategy) {
        val lowerBuyOrder = orderRepository.findLowerOpenBuyOrderForStrategy(strategy.id)
        val buyPrice = GridStrategyUtils.getNextBuyOrderPrice(lowerBuyOrder!!.order.price, strategy.settings)
        placeOrder(buyPrice, OrderSide.BUY, strategy)
    }

    private fun placeNewHigherBuyOrderBasedOnFilled(filledOrder: FuturesLimitOrder, strategy: GridStrategy) {
        val higherBuyOrder = orderRepository.findHigherOpenBuyOrderForStrategy(strategy.id)
        val buyPrice = BigDecimalUtils.avg(filledOrder.price, higherBuyOrder!!.order.price)
        placeOrder(buyPrice, OrderSide.BUY, strategy)
    }

    private fun placeNewHigherSellOrder(strategy: GridStrategy) {
        val higherSellOrder = orderRepository.findHigherOpenSellOrderForStrategy(strategy.id)
        val sellPrice = GridStrategyUtils.getNextSellOrderPrice(higherSellOrder!!.order.price, strategy.settings)
        placeOrder(sellPrice, OrderSide.SELL, strategy)
    }

    private fun placeLeadOffBuyOrders(startPrice: BigDecimal, strategy: GridStrategy) {
        var lastPrice = startPrice
        for (i in 0 until NUM_OF_ORDERS / 2) {
            lastPrice = GridStrategyUtils.getNextBuyOrderPrice(lastPrice, strategy.settings)
            placeOrder(lastPrice, OrderSide.BUY, strategy)
        }
    }

    private fun placeLeadOffSellOrders(startPrice: BigDecimal, strategy: GridStrategy) {
        var lastPrice = startPrice
        for (i in 0 until NUM_OF_ORDERS / 2) {
            lastPrice = GridStrategyUtils.getNextSellOrderPrice(lastPrice, strategy.settings)
            placeOrder(lastPrice, OrderSide.SELL, strategy)
        }
    }

    private fun placeOrder(price: BigDecimal, side: OrderSide, strategy: GridStrategy) {
        val settings = strategy.settings
        val quality = when (side) {
            OrderSide.SELL -> settings.sellOrderQuantity; OrderSide.BUY -> settings.buyOrderQuantity
        }
        val orderQuery = LimitOrderQuery(settings.symbol, side, price, quality)
        val order = futuresService.placeLimitOrder(orderQuery)
        orderRepository.associateOrderWithStrategy(strategy.id, order.id)
    }

    private fun cancelHigherSellOrder(strategyId: Long) {
        val higherSellOrder = orderRepository.findHigherOpenSellOrderForStrategy(strategyId)
        higherSellOrder?.let { futuresService.cancelOrder(it.order.symbol, it.order.exchangeId) }
    }

    private fun cancelLowerBuyOrder(strategyId: Long) {
        val lowerBuyOrder = orderRepository.findLowerOpenBuyOrderForStrategy(strategyId)
        lowerBuyOrder?.let { futuresService.cancelOrder(it.order.symbol, it.order.exchangeId) }
    }
}