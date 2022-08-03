package criptocatbox.provider

import criptocatbox.domain.CurrencyPair
import criptocatbox.domain.PairBestPrices

interface MarketDataProvider {

    fun getBestPrices(currencyPair: CurrencyPair): PairBestPrices?

    fun getAllAllowedPairs(): Set<CurrencyPair>

}