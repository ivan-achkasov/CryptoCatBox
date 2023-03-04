package cryptocatbox.service.arbitrage

import cryptocatbox.domain.ArbitrageChain
import java.util.LinkedList

abstract class AbstractArbitrageSignalProducer: ArbitrageSignalProducer {
    private val subscriptions: LinkedList<(arbitrageChain: ArbitrageChain, spread: Double) -> Unit> = LinkedList()

    override fun subscribe(alertBlock: (arbitrageChain: ArbitrageChain, spread: Double) -> Unit) {
        synchronized(subscriptions) {
            subscriptions.add(alertBlock)
        }
    }

    protected fun notifySubscribers(arbitrageChain: ArbitrageChain, spread: Double) {
        synchronized(subscriptions) {
            subscriptions.forEach { alertFun ->
                alertFun(arbitrageChain, spread)
            }
        }
    }
}