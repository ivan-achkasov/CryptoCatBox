package cryptocatbox.service.arbitrage

import cryptocatbox.domain.SingleExchangeChain
import cryptocatbox.provider.PriceProvider
import kotlinx.coroutines.*

class SingleExchangeSignalObserver(
    private val coroutineScope: CoroutineScope,
    private val arbitrageChain: SingleExchangeChain,
    private val priceProvider: PriceProvider
): AbstractArbitrageSignalProducer() {

    /*override fun start() {
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
    }*/
}