package criptocatbox.exchange.pancakeswap.provider

import criptocatbox.domain.*
import criptocatbox.exchange.pancakeswap.client.PancakeSwapHttpClient
import criptocatbox.provider.PriceProvider
import org.springframework.stereotype.Service

@Service
class PancakeswapPriceProvider(private val client: PancakeSwapHttpClient): PriceProvider {

    override fun getPrice(currencyPair: CurrencyPair): PairPrice? {
        val price = client.getTopPairs().data.values.find {
            it.baseSymbol.uppercase() == currencyPair.buy.toString() && it.quoteSymbol.uppercase() == currencyPair.sell.toString()
        }?.let { PairPrice(currencyPair, it.price) }
        if(price != null) {
            return price
        }
        return client.getTopPairs().data.values.find {
            it.baseSymbol.uppercase() == currencyPair.sell.toString() && it.quoteSymbol.uppercase() == currencyPair.buy.toString()
        }?.let { PairPrice(currencyPair, it.price) }
    }

}