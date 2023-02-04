package criptocatbox.exchange.pancakeswap.client.dto

import com.fasterxml.jackson.annotation.JsonAlias

data class PairData(
    @JsonAlias("base_symbol")
    val baseSymbol: String,

    @JsonAlias("quote_symbol")
    val quoteSymbol: String,
    val price: Double
)
