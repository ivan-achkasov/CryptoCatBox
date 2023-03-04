package cryptocatbox.repository

import cryptocatbox.common.domain.CurrencyPair
import cryptocatbox.domain.PairBestPrices

interface MarketDataRepository {
    fun saveBestPrices(bestPricesDto: PairBestPrices)

    fun getBestPrice(pair: CurrencyPair): PairBestPrices?

    fun getAllPairs(): Set<CurrencyPair>
}