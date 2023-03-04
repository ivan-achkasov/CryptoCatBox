package cryptocatbox.common.domain

fun parsePair(value: String, delimiter: Char): CurrencyPair {
    if(value.startsWith("1INCH")) {
        return CurrencyPair(Currency.ONE_INCH, Currency.valueOf(value.substring(6)))
    }
    if(value.endsWith("1INCH")) {
        return CurrencyPair(Currency.valueOf(value.substring(0, value.length - 5)), Currency.ONE_INCH)
    }
    val parts = value.split(delimiter)
    if(parts.size != 2) {
        throw IllegalArgumentException("Could not parse pair: $value.")
    }
    return CurrencyPair(Currency.valueOf(parts.first()), Currency.valueOf(parts.last()))
}

fun parsePairWithoutDelimiter(value: String): CurrencyPair {
    return getPair(value, 1)
}

private fun getPair(value: String, index: Int): CurrencyPair {
    if(value.startsWith("1INCH")) {
        return CurrencyPair(Currency.ONE_INCH, Currency.valueOf(value.substring(5)))
    }
    if(value.endsWith("1INCH")) {
        return CurrencyPair(Currency.valueOf(value.substring(0, value.length - 5)), Currency.ONE_INCH)
    }
    return try{
        tryGetPair(value, index)
    } catch (e: IllegalArgumentException) {
        if(index + 1 > value.length) {
            throw e
        }
        getPair(value, index + 1)
    }
}

private fun tryGetPair(value: String, index: Int): CurrencyPair {
    val firstPart = value.substring(0, index)
    val lastPart = value.substring(index)
    return CurrencyPair(Currency.valueOf(firstPart), Currency.valueOf(lastPart))
}