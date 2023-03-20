package cryptocatbox.domain

import cryptocatbox.common.domain.CurrencyPair

data class PairBestPrices(
    val currencyPair: CurrencyPair,
    val bidPrice: Double,
    val askPrice: Double
) {
    fun invert(): PairBestPrices {
        return PairBestPrices(currencyPair.invert(), 1.0/askPrice, 1.0/bidPrice)
    }
}
