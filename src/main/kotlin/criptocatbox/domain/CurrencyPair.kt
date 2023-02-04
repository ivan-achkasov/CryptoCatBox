package criptocatbox.domain

data class CurrencyPair(val buy: Currency, val sell: Currency) {
    init {
        if(buy == sell) {
            throw IllegalArgumentException("Invalid pair, the same currencies.")
        }
    }

    fun invert(): CurrencyPair {
        return CurrencyPair(sell, buy)
    }

    fun toString(delimiter: Char): String {
        return "$buy$delimiter$sell"
    }

    override fun hashCode(): Int {
        return buy.hashCode() + sell.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CurrencyPair

        if((buy == other.buy && sell == other.sell) ||
            (buy == other.sell && sell == other.buy))
            return true

        return false
    }


}
