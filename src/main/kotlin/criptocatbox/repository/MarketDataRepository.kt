package criptocatbox.repository

import criptocatbox.domain.CurrencyPair
import criptocatbox.domain.PairBestPrices

interface MarketDataRepository {
    fun saveBestPrices(bestPricesDto: PairBestPrices)

    fun getBestPrice(pair: CurrencyPair): PairBestPrices?

    fun getAllPairs(): Set<CurrencyPair>
}