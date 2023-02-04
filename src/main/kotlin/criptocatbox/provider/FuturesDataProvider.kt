package criptocatbox.provider

import criptocatbox.domain.Candle
import criptocatbox.domain.CurrencyPair

interface FuturesDataProvider {
    fun getLast1MinCandles(pair: CurrencyPair, limit: Short = 500): List<Candle>
    fun getCandles(pair: CurrencyPair, interval: String, startTime: Long, endTime: Long?): List<Candle>
}