package criptocatbox.service.impl

import criptocatbox.domain.Exchange
import criptocatbox.domain.SingleExchangeChain
import criptocatbox.service.ArbitrageChainSubscriber
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ConsoleSubscriber : ArbitrageChainSubscriber {

    override fun notifyFoundSingleExchangeSpread(
        exchange: Exchange,
        arbitrageChain: SingleExchangeChain,
        spread: Double
    ) {
        if (spread > 1) {
            println("$exchange: ${arbitrageChain.first}->${arbitrageChain.second}->${arbitrageChain.third} $spread")
        }
    }

}