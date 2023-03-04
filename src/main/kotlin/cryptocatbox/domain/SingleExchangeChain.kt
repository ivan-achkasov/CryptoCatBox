package cryptocatbox.domain

import cryptocatbox.common.domain.Currency

data class SingleExchangeChain(val first: Currency, val second: Currency, val third: Currency) {

    override fun hashCode(): Int {
        return first.hashCode() + second.hashCode() + third.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SingleExchangeChain

        if(
            (first == other.first && second == other.second && third == other.third) ||
            (first == other.second && second == other.third && third == other.first) ||
            (first == other.third && second == other.first && third == other.second)
        )
            return true

        return false
    }


}
