package criptocatbox.config

import criptocatbox.Logging
import criptocatbox.binance.provider.BinanceMarketDataProvider
import criptocatbox.domain.Exchange
import criptocatbox.logger
import criptocatbox.service.impl.ChainsCreator
import criptocatbox.service.impl.SingleExchangeChainsObserversMediator
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class SetBinanceChainsConfig(
    private val binanceMarketDataProvider: BinanceMarketDataProvider,
    private val chainsCreator: ChainsCreator,
    private val singleExchangeChainsObserversMediator: SingleExchangeChainsObserversMediator,
    @Value("\${binance.data_load.delay:15000}")
    private val createBinanceChinsDelay: Long
) : InitializingBean, Logging {

    override fun afterPropertiesSet() {
        Thread {
            Thread.sleep(createBinanceChinsDelay)
            logger().info("Loading allowed chains for Binance")
            val chains = chainsCreator.createSingleExchangeChains(binanceMarketDataProvider.getAllAllowedPairs())
            chains.forEach { singleExchangeChainsObserversMediator.addChain(Exchange.BINANCE, it) }
            logger().info("Loaded ${chains.size} chains for Binance")
        }.start()
    }
}