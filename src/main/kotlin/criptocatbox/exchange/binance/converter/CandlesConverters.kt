package criptocatbox.exchange.binance.converter

import criptocatbox.domain.Candle
import criptocatbox.domain.CurrencyPair
import criptocatbox.exchange.binance.client.dto.KLineDto

fun KLineDto.toDomainModel(pair: CurrencyPair): Candle {
    return Candle(
        pair,
        this.data.startTime,
        this.data.closeTime,
        this.data.openPrice,
        this.data.closePrice,
        this.data.closed
    )
}