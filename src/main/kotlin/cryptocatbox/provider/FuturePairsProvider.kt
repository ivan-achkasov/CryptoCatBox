package cryptocatbox.provider

import cryptocatbox.common.domain.CurrencyPair

interface FuturePairsProvider {

    fun getAllowedPairs(): Set<CurrencyPair>

}