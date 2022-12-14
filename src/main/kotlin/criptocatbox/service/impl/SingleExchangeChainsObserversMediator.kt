package criptocatbox.service.impl

import criptocatbox.domain.SingleExchangeChain
import criptocatbox.domain.Exchange
import criptocatbox.provider.MarketDataProvider
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import org.springframework.stereotype.Service
import java.util.LinkedList
import java.util.concurrent.Executors
import javax.annotation.PreDestroy
import kotlin.coroutines.EmptyCoroutineContext

@Service
class SingleExchangeChainsObserversMediator(private val marketDataProviders: Map<Exchange, MarketDataProvider>) {

    private val coroutineScope = CoroutineScope(Executors.newWorkStealingPool().asCoroutineDispatcher())

    private val searchers = LinkedList<SingleExchangeChainObserver>()
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

        val searcher = SingleExchangeChainObserver(coroutineScope, chain, dataProvider)
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
    }


}