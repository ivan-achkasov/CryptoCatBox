package criptocatbox.service.arbitrage

import criptocatbox.domain.ArbitrageChain

interface ArbitrageSignalProducer {
    fun subscribe(alertBlock: (arbitrageChain: ArbitrageChain, spread: Double) -> Unit)
}