package criptocatbox.domain

data class CurrencyPair(val buy: Currency, val sale: Currency) {
    init {
        if(buy == sale) {
            throw IllegalArgumentException("Invalid pair, the same currencies.")
        }
    }

    fun invert(): CurrencyPair {
        return CurrencyPair(sale, buy)
    }

    fun toString(delimiter: Char): String {
        return "$buy$delimiter$sale"
    }
}
