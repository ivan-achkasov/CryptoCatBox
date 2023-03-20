package cryptocatbox.provider

import cryptocatbox.common.domain.CurrencyPair
import cryptocatbox.domain.PairPrice

interface PriceProvider {

    fun getPrice(currencyPair: CurrencyPair): PairPrice?

}