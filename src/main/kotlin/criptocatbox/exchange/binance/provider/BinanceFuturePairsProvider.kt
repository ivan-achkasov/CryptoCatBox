package criptocatbox.exchange.binance.provider

import criptocatbox.domain.Currency
import criptocatbox.domain.CurrencyPair
import criptocatbox.exchange.binance.client.BinanceFuturesHttpClient
import criptocatbox.provider.FuturePairsProvider
import org.springframework.stereotype.Service

@Service
class BinanceFuturePairsProvider(private val futuresHttpClient: BinanceFuturesHttpClient): FuturePairsProvider {

    override fun getAllowedPairs(): Set<CurrencyPair> {
        val symbols = futuresHttpClient.getExchangeInfo().symbols
        val result = HashSet<CurrencyPair>(symbols.size)
        symbols.forEach {
            try {
                result.add( CurrencyPair(Currency.customValueOf(it.baseAsset), Currency.customValueOf(it.marginAsset)))
            } catch (ex: Exception) {
                //TODO: logging
            }
        }
        return result
    }
}