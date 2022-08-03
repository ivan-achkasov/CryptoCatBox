package criptocatbox.config

import criptocatbox.domain.Exchange
import criptocatbox.provider.MarketDataProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MarketDataProvidersConfig {

    @Bean
    fun createMarketDataProvidersMapByExchange(marketDataProviders: List<MarketDataProvider>):
            Map<Exchange, MarketDataProvider> {
        return mapOf(Exchange.BINANCE to marketDataProviders.first())
    }
}