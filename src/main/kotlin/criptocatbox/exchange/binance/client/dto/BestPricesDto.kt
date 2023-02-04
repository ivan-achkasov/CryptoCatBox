package criptocatbox.exchange.binance.client.dto

import com.fasterxml.jackson.annotation.JsonAlias

data class BestPricesDto(
    @JsonAlias("s")
    val symbol: String,
    @JsonAlias("b")
    val bestBidPrice: Double,
    @JsonAlias("a")
    val bestAskPrice: Double
)
