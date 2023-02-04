package criptocatbox.exchange.binance.client.dto

data class ExchangeInfoDto(val symbols: List<Symbols>) {

    data class Symbols(
        val baseAsset: String,
        val marginAsset: String,
        val pricePrecision: Int,
        val quantityPrecision: Int
    )
}