package criptocatbox.provider

import criptocatbox.domain.CurrencyPair

interface PairsProvider {
    fun getAllowedPairs(): Set<CurrencyPair>
}