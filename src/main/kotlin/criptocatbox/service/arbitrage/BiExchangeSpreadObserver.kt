package criptocatbox.service.arbitrage

import criptocatbox.domain.Currency
import criptocatbox.domain.CurrencyPair
import criptocatbox.domain.Exchange
import criptocatbox.provider.PairsProvider
import org.springframework.beans.factory.InitializingBean

//@Service
class BiExchangeSpreadObserver(
    private val pairsProviders: Map<Exchange, PairsProvider>,
    private val priceDifferenceChecker: PriceDifferenceChecker
) : InitializingBean {

    override fun afterPropertiesSet() {
        startTest()
    }

    private fun startTest() {
        Thread {
            while (true) {
                val ignored = listOf(Currency.DNT, Currency.LIT)
                val pairs = HashSet<CurrencyPair>()
                for (provider in pairsProviders.values) {
                    pairs.addAll(provider.getAllowedPairs())
                }
                pairs.removeAll {
                    ignored.contains(it.buy) || ignored.contains(it.sell)
                }
                for (pair in pairs) {
                    val spread = priceDifferenceChecker.getSpread(pair)
                    if (spread?.spread != null && spread.spread > 1) {
                        println(spread)
                    }
                }

                Thread.sleep(5000)
            }
        }.apply {
            this.isDaemon = true
        }.start()
    }
}