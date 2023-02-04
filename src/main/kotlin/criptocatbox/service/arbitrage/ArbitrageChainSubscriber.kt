package criptocatbox.service.arbitrage

import criptocatbox.domain.Exchange
import criptocatbox.domain.SingleExchangeChain

interface ArbitrageChainSubscriber {

    fun notifyFoundSingleExchangeSpread(exchange: Exchange, arbitrageChain: SingleExchangeChain, spread: Double)

}