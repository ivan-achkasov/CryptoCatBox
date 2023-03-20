package cryptocatbox.service.arbitrage

import cryptocatbox.domain.ArbitrageChain

interface ArbitrageSignalProducer {
    fun subscribe(alertBlock: (arbitrageChain: ArbitrageChain, spread: Double) -> Unit)
}