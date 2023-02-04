package criptocatbox.config

import criptocatbox.domain.Exchange
import criptocatbox.exchange.binance.provider.BinancePairsProvider
import criptocatbox.exchange.binance.provider.BinancePriceProvider
import criptocatbox.exchange.pancakeswap.provider.PancakeSwapPairsProvider
import criptocatbox.exchange.pancakeswap.provider.PancakeswapPriceProvider
import criptocatbox.exchange.whitebit.provider.WhiteBitPairsProvider
import criptocatbox.exchange.whitebit.provider.WhiteBitPriceProvider
import criptocatbox.provider.PairsProvider
import criptocatbox.provider.PriceProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ExchangesProvidersConfig {

    @Bean
    fun createPriceProvidersMapByExchange(priceProviders: List<PriceProvider>):
            Map<Exchange, PriceProvider> {
        //TODO: fix O(n^2) complexity
        return mapOf(
            Exchange.BINANCE to priceProviders.first { it is BinancePriceProvider },
            Exchange.WHITE_BIT to priceProviders.first { it is WhiteBitPriceProvider },
            Exchange.PANCAKE_SWAP to priceProviders.first { it is PancakeswapPriceProvider }
        )
    }


    @Bean
    fun createPairsProvidersMapByExchange(pairsProviders: List<PairsProvider>):
            Map<Exchange, PairsProvider> {
        //TODO: fix O(n^2) complexity
        return mapOf(
            Exchange.BINANCE to pairsProviders.first { it is BinancePairsProvider },
            Exchange.WHITE_BIT to pairsProviders.first { it is WhiteBitPairsProvider },
            Exchange.PANCAKE_SWAP to pairsProviders.first { it is PancakeSwapPairsProvider }
        )
    }
}