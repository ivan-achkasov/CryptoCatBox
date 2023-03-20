package cryptocatbox.domain

import cryptocatbox.common.domain.CurrencyPair

data class PairPrice(val pair: CurrencyPair, val price: Double)