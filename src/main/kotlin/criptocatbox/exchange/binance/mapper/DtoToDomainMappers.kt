package criptocatbox.exchange.binance.mapper

import criptocatbox.domain.CurrencyPair
import criptocatbox.domain.PairBestPrices
import criptocatbox.exchange.binance.client.dto.BestPricesDto

fun BestPricesDto.mapToDomain(parsePairFun: (value: String) -> CurrencyPair): PairBestPrices {
    return PairBestPrices(parsePairFun(this.symbol), this.bestBidPrice, this.bestAskPrice)
}
