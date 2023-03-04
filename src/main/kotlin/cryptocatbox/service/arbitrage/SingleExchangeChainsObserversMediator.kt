package cryptocatbox.service.arbitrage

import cryptocatbox.domain.Exchange
import cryptocatbox.provider.PriceProvider
import org.springframework.stereotype.Service

@Service
class SingleExchangeChainsObserversMediator(private val marketDataProviders: Map<Exchange, PriceProvider>):
    AbstractArbitrageSignalProducer() {

    /*private val coroutineScope = CoroutineScope(Executors.newWorkStealingPool().asCoroutineDispatcher())

    private val searchers = LinkedList<SingleExchangeSignalObserver>()
    private val subscriptions =
        LinkedList<(exchange: Exchange, arbitrageChain: SingleExchangeChain, spread: Double) -> Unit>()

    @PreDestroy
    fun beforeDestroy() {
        coroutineScope.cancel()
    }

    fun subscribe(block: (exchange: Exchange, arbitrageChain: SingleExchangeChain, spread: Double) -> Unit) {
        synchronized(subscriptions) {
            subscriptions.add(block)
        }
        synchronized(searchers) {
            searchers.forEach {
                it.subscribe { chain, spread ->
                    block(Exchange.BINANCE, chain, spread)
                }
            }
        }
    }

    fun addChain(exchange: Exchange, chain: SingleExchangeChain) {
        val dataProvider = marketDataProviders[exchange]
            ?: throw RuntimeException("There is not data provider for this exchange: $exchange")

        val searcher = SingleExchangeSignalObserver(coroutineScope, chain, dataProvider)
        synchronized(searchers) {
            searchers.add(searcher)
        }
        synchronized(subscriptions) {
            subscriptions.forEach {
                searcher.subscribe { chain, spread ->
                    it(Exchange.BINANCE, chain, spread)
                }
            }
        }
        searcher.start()
    }*/
}