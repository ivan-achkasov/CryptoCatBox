package criptocatbox.service

import criptocatbox.domain.SingleExchangeChain

interface ArbitrageChainObserver {
    fun subscribe(alertBlock: (arbitrageChain: SingleExchangeChain, spread: Double) -> Unit)
    fun start()
}