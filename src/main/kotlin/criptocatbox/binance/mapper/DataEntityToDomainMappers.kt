package criptocatbox.binance.mapper

import criptocatbox.binance.data.entity.BestPrices
import criptocatbox.domain.CurrencyPair
import criptocatbox.domain.PairBestPrices

fun BestPrices.mapToDomain(parsePairFun: (value: String) -> CurrencyPair): PairBestPrices {
    return PairBestPrices(parsePairFun(this.pair), this.bestBidPrice, this.bestAskPrice)
}