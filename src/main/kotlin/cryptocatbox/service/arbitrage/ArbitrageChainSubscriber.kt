package cryptocatbox.service.arbitrage

import cryptocatbox.domain.Exchange
import cryptocatbox.domain.SingleExchangeChain

interface ArbitrageChainSubscriber {

    fun notifyFoundSingleExchangeSpread(exchange: Exchange, arbitrageChain: SingleExchangeChain, spread: Double)

}