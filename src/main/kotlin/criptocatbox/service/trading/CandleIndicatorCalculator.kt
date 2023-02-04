package criptocatbox.service.trading

import criptocatbox.domain.Candle

interface CandleIndicatorCalculator {
    fun getValue(candles: List<Candle>): Double
}