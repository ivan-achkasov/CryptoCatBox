package criptocatbox.binance.mapper

import criptocatbox.binance.data.entity.BestPrices
import criptocatbox.binance.dto.BestPricesDto
import criptocatbox.domain.CurrencyPair
import criptocatbox.domain.parsePair
import criptocatbox.domain.parsePairWithoutDelimiter

fun BestPricesDto.mapToDataEntity(entityPairDelimiter: Char): BestPrices {
    val pair = parsePairWithoutDelimiter(this.symbol)
    return BestPrices(pair.toString(entityPairDelimiter), this.bestBidPrice, this.bestAskPrice)
}