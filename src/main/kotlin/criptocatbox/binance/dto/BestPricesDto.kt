package criptocatbox.binance.dto

import com.fasterxml.jackson.annotation.JsonAlias
import criptocatbox.domain.Currency
import criptocatbox.domain.CurrencyPair

data class BestPricesDto(
    @JsonAlias("s")
    val symbol: String,
    @JsonAlias("b")
    val bestBidPrice: Double,
    @JsonAlias("a")
    val bestAskPrice: Double
)
