package criptocatbox.provider

import criptocatbox.domain.CurrencyPair
import criptocatbox.domain.PairBestPrices
import criptocatbox.domain.PairPrice

interface PriceProvider {

    fun getPrice(currencyPair: CurrencyPair): PairPrice?

}