package criptocatbox.binance.data.entity

data class BestPrices(
    val pair: String,
    val bestBidPrice: Double,
    val bestAskPrice: Double
)
