package cryptocatbox.provider

import cryptocatbox.common.domain.CurrencyPair

interface PairsProvider {
    fun getAllowedPairs(): Set<CurrencyPair>
}