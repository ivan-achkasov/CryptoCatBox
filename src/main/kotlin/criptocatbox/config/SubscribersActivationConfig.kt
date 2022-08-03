package criptocatbox.config

import criptocatbox.service.ArbitrageChainSubscriber
import criptocatbox.service.impl.SingleExchangeChainsObserversMediator
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.Configuration

@Configuration
class SubscribersActivationConfig (
    private val subscribers: List<ArbitrageChainSubscriber>,
    private val singleExchangeChainsObserversMediator: SingleExchangeChainsObserversMediator
) : InitializingBean {

    override fun afterPropertiesSet() {
        subscribers.forEach {
            singleExchangeChainsObserversMediator.subscribe(it::notifyFoundSingleExchangeSpread)
        }
    }

}