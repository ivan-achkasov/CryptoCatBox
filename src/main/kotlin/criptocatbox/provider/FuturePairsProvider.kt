package criptocatbox.provider

import criptocatbox.domain.CurrencyPair

interface FuturePairsProvider {

    fun getAllowedPairs(): Set<CurrencyPair>

}