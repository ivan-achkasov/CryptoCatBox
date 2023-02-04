package criptocatbox.exchange.whitebit.provider

import criptocatbox.domain.CurrencyPair
import criptocatbox.domain.PairPrice
import criptocatbox.exchange.whitebit.client.WhiteBitHttpClient
import criptocatbox.provider.PriceProvider
import org.springframework.stereotype.Service

@Service
class WhiteBitPriceProvider(private val httpClient: WhiteBitHttpClient): PriceProvider {

    companion object {
        private const val DEFAULT_PAIRS_DELIMITER = '_'
    }

    override fun getPrice(currencyPair: CurrencyPair): PairPrice? {
        val tickers = httpClient.getTickers()
        val pairTicker =
            httpClient.getTickers().pairsData["${currencyPair.buy}$DEFAULT_PAIRS_DELIMITER${currencyPair.sell}"] ?:
            httpClient.getTickers().pairsData["${currencyPair.sell}$DEFAULT_PAIRS_DELIMITER${currencyPair.buy}"]

        return pairTicker?.let { PairPrice(currencyPair, it.lastPrice) }
    }

}