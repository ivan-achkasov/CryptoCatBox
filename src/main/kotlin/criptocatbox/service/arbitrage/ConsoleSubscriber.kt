package criptocatbox.service.arbitrage

import criptocatbox.domain.Exchange
import criptocatbox.domain.SingleExchangeChain
import org.springframework.stereotype.Component
import java.util.Date

@Component
class ConsoleSubscriber : ArbitrageChainSubscriber {

    override fun notifyFoundSingleExchangeSpread(
        exchange: Exchange,
        arbitrageChain: SingleExchangeChain,
        spread: Double
    ) {
        if (spread > 1) {
            println("${Date()} - $exchange: ${arbitrageChain.first}->${arbitrageChain.second}->${arbitrageChain.third} $spread")
        }
    }

}