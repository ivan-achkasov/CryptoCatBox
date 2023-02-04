package criptocatbox.service.arbitrage

import criptocatbox.domain.ArbitrageNode
import criptocatbox.domain.BiExchangeSpread
import criptocatbox.domain.CurrencyPair
import criptocatbox.domain.Exchange
import criptocatbox.provider.PriceProvider
import org.springframework.stereotype.Component

@Component
class PriceDifferenceChecker(
    private val priceProviders: Map<Exchange, PriceProvider>
) {
    fun getSpread(pair: CurrencyPair): BiExchangeSpread? {
        var maxPrice = 0.0
        var maxPriceExchange: Exchange? = null

        var minPrice = Double.MAX_VALUE
        var minPriceExchange: Exchange? = null

        for((exchange, provider) in priceProviders.entries) {
            val currentAskPrice: Double = provider.getPrice(pair)?.price ?: continue
            val currentPrice: Double = provider.getPrice(pair)?.price ?: continue

            if (currentAskPrice < minPrice) {
                minPrice = currentAskPrice
                minPriceExchange = exchange
            }
                if (currentPrice > maxPrice) {
                maxPrice = currentPrice
                maxPriceExchange = exchange
            }
        }

        if(maxPriceExchange == null || minPriceExchange == null || maxPriceExchange == minPriceExchange) {
            return null
        }

        val spread = (maxPrice - minPrice) / minPrice

        return BiExchangeSpread(
            ArbitrageNode(minPriceExchange, pair.buy),
            ArbitrageNode(maxPriceExchange, pair.sell),
            spread
        )
    }
}