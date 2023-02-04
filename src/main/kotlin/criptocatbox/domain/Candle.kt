package criptocatbox.domain

data class Candle(
    val pair: CurrencyPair,
    val startTime: Long,
    val closeTime: Long,
    val openPrice: Double,
    val closePrice: Double,
    val closed: Boolean
)
