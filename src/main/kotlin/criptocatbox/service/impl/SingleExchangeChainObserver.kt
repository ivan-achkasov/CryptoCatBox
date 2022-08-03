package criptocatbox.service.impl

import criptocatbox.domain.SingleExchangeChain
import criptocatbox.domain.CurrencyPair
import criptocatbox.provider.MarketDataProvider
import criptocatbox.service.ArbitrageChainObserver
import kotlinx.coroutines.*
import java.util.LinkedList

class SingleExchangeChainObserver(
    private val coroutineScope: CoroutineScope,
    private val arbitrageChain: SingleExchangeChain,
    private val marketDataProvider: MarketDataProvider
) : ArbitrageChainObserver {
    private val subscriptions: LinkedList<(arbitrageChain: SingleExchangeChain, spread: Double) -> Unit> = LinkedList()

    override fun subscribe(alertBlock: (arbitrageChain: SingleExchangeChain, spread: Double) -> Unit) {
        synchronized(subscriptions) {
            subscriptions.add(alertBlock)
        }
    }

    override fun start() {
        coroutineScope.launch {
            while (true) {
                val bestPricesFirstToSecond =
                    marketDataProvider.getBestPrices(CurrencyPair(arbitrageChain.first, arbitrageChain.third))!!
                val bestPricesSecondToThird =
                    marketDataProvider.getBestPrices(CurrencyPair(arbitrageChain.second, arbitrageChain.first))!!
                val bestPricesThirdToFirst =
                    marketDataProvider.getBestPrices(CurrencyPair(arbitrageChain.third, arbitrageChain.second))!!

                val initCurrencyAmount = 1000.0

                val gotFirst: Double = initCurrencyAmount / bestPricesFirstToSecond.askPrice * 0.999
                val gotSecond: Double = gotFirst / bestPricesSecondToThird.askPrice * 0.999
                val gotInitCurrency: Double = gotSecond / bestPricesThirdToFirst.askPrice * 0.999

                if (initCurrencyAmount < gotInitCurrency) {
                    val spread = (gotInitCurrency - initCurrencyAmount) / initCurrencyAmount * 100
                    notifySubscribers(arbitrageChain, spread)
                }
                delay(1)
            }
        }
    }

    private fun notifySubscribers(arbitrageChain: SingleExchangeChain, spread: Double) {
        synchronized(subscriptions) {
            subscriptions.forEach { alertFun ->
                alertFun(arbitrageChain, spread)
            }
        }
    }
}