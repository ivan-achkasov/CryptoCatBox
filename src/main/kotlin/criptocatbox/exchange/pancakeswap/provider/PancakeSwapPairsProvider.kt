package criptocatbox.exchange.pancakeswap.provider

import criptocatbox.domain.Currency
import criptocatbox.domain.CurrencyPair
import criptocatbox.exchange.pancakeswap.client.PancakeSwapHttpClient
import criptocatbox.provider.PairsProvider
import org.springframework.stereotype.Service

@Service
class PancakeSwapPairsProvider(private val httpClient: PancakeSwapHttpClient) : PairsProvider {
    override fun getAllowedPairs(): Set<CurrencyPair> {
        val pairs = mutableSetOf<CurrencyPair>()
        httpClient.getTopPairs().data.values.forEach {
            try {
                pairs.add(
                    CurrencyPair(
                        Currency.valueOf(it.baseSymbol.uppercase()),
                        Currency.valueOf(it.quoteSymbol.uppercase())
                    )
                )
            } catch (e: Exception) {

            }
        }
        return pairs
    }
}