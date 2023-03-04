package cryptocatbox.exchange.binance.service.strategy

import cryptocatbox.exchange.binance.domain.strategy.GridStrategy
import cryptocatbox.exchange.binance.dto.*
import cryptocatbox.exchange.binance.dto.OrderSide.BUY
import cryptocatbox.exchange.binance.dto.OrderSide.SELL
import cryptocatbox.exchange.binance.events.FuturesOrdersEventsManager
import cryptocatbox.exchange.binance.events.dto.OrderUpdatedEvent
import cryptocatbox.exchange.binance.service.BinanceFuturesService
import java.math.BigDecimal
import java.util.LinkedList

class GridStrategyProcessor(
    private val futuresService: BinanceFuturesService,
    private val futuresOrdersEventsManager: FuturesOrdersEventsManager,
    strategy: GridStrategy
) {
    private val settings = strategy.settings

    private val buyOrderPriceMultiplier = BigDecimal.ONE - (settings.stepMultiplier.divide((100.0).toBigDecimal()))
    private val sellOrderPriceMultiplier =  BigDecimal.ONE + (settings.stepMultiplier.divide((100.0).toBigDecimal()))

    private val buyOrders: LinkedList<LimitOrder> = LinkedList()
    private val sellOrders: LinkedList<LimitOrder> = LinkedList()

    fun start() {
        futuresOrdersEventsManager.subscribe(this::onOrderUpdate)

        placeLeadOffOrders()
    }

    private fun onOrderUpdate(orderEvent: OrderUpdatedEvent) {
        if(orderEvent.status != OrderStatus.FILLED) {
            return
        }

        val order = buyOrders.find { it.id == orderEvent.orderId }
            ?: sellOrders.find { it.id == orderEvent.orderId }
            ?: return

        placeNewOrdersAfterFillingOrder(order)
    }

    private fun placeLeadOffOrders() {
        placeLeadOffBuyOrders()
        placeLeadOffSellOrders()
    }

    private fun placeLeadOffBuyOrders() {
        var lastBuyPrice = settings.startPrice
        for (i in 0 until settings.numOfStartOrders / 2) {
            lastBuyPrice *= buyOrderPriceMultiplier
            val orderQuery = LimitOrderQuery(settings.pair, BUY, lastBuyPrice, settings.buyOrderQuantity)
            val order = futuresService.placeLimitOrder(orderQuery)
            addNewLowerBuyOrder(order)
        }
    }

    private fun placeLeadOffSellOrders() {
        var lastBuyPrice = settings.startPrice
        for (i in 0 until settings.numOfStartOrders / 2) {
            lastBuyPrice *= sellOrderPriceMultiplier
            val orderQuery = LimitOrderQuery(settings.pair, SELL, lastBuyPrice, settings.sellOrderQuantity)
            val order = futuresService.placeLimitOrder(orderQuery)
            addNewHigherSellOrder(order)
        }
    }

    private fun placeNewOrdersAfterFillingOrder(filledOrder: LimitOrder) {
        when (filledOrder.side) {
            BUY -> {
                placeNewLowerSellOrderBasedOnFilled(filledOrder)
                placeNewLowerBuyOrder()
            }
            SELL -> {
                placeNewHigherBuyOrderBasedOnFilled(filledOrder)
                placeNewHigherSellOrder()
            }
        }
    }

    private fun placeNewLowerSellOrderBasedOnFilled(filledOrder: LimitOrder) {
        val lowerSellOrder = getLowerSellOrder()
        val sellPrice = filledOrder.price.plus(lowerSellOrder.price).divide(2.0.toBigDecimal())
        val sellOrderQuery = LimitOrderQuery(settings.pair, SELL, sellPrice, settings.sellOrderQuantity)
        val sellOrder = futuresService.placeLimitOrder(sellOrderQuery)
        addLowerSellOrder(sellOrder)
    }

    private fun placeNewLowerBuyOrder() {
        val lowerBuyOrder = getLowerBuyOrder()
        val buyPrice = lowerBuyOrder.price * buyOrderPriceMultiplier
        val buyOrderQuery = LimitOrderQuery(settings.pair, BUY, buyPrice, settings.buyOrderQuantity)
        val buyOrder = futuresService.placeLimitOrder(buyOrderQuery)
        addNewLowerBuyOrder(buyOrder)
    }

    private fun placeNewHigherBuyOrderBasedOnFilled(filledOrder: LimitOrder) {
        val higherBuyOrder = getHigherBuyOrder()
        val buyPrice = filledOrder.price.plus(higherBuyOrder.price).divide(2.0.toBigDecimal())
        val orderQuery = LimitOrderQuery(settings.pair, BUY, buyPrice, settings.buyOrderQuantity)
        val order = futuresService.placeLimitOrder(orderQuery)
        addNewHigherBuyOrder(order)
    }

    private fun placeNewHigherSellOrder() {
        val higherSellOrder = getHigherSellOrder()
        val sellPrice = higherSellOrder.price * sellOrderPriceMultiplier
        val orderQuery = LimitOrderQuery(settings.pair, SELL, sellPrice, settings.sellOrderQuantity)
        val order = futuresService.placeLimitOrder(orderQuery)
        addNewHigherSellOrder(order)
    }

    private fun addNewHigherBuyOrder(order: LimitOrder) {
        buyOrders.addFirst(order)
    }

    private fun addNewLowerBuyOrder(order: LimitOrder) {
        buyOrders.addLast(order)
    }

    private fun getLowerBuyOrder(): LimitOrder {
        return buyOrders.last()
    }

    private fun getHigherBuyOrder(): LimitOrder {
        return buyOrders.first()
    }

    private fun addLowerSellOrder(order: LimitOrder) {
        sellOrders.addFirst(order)
    }

    private fun addNewHigherSellOrder(order: LimitOrder) {
        sellOrders.addLast(order)
    }

    private fun getLowerSellOrder(): LimitOrder {
        return sellOrders.first()
    }

    private fun getHigherSellOrder(): LimitOrder {
        return sellOrders.last()
    }
}