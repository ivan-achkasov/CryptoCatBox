package criptocatbox.exchange.whitebit.client.dto

import com.fasterxml.jackson.annotation.JsonAlias

data class TickerPairData (
    @JsonAlias("last_price")
    val lastPrice: Double
)