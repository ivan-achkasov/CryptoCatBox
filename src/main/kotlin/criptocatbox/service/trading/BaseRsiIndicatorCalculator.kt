package criptocatbox.service.trading

import criptocatbox.domain.Candle

abstract class BaseRsiIndicatorCalculator: CandleIndicatorCalculator {

    override fun getValue(candles: List<Candle>): Double {
        val rs = calculateRelativeStrength(candles)
        return 100.0 - (100.0 / (1.0 + rs))
    }

    protected abstract fun calculateRelativeStrength(candles: List<Candle>): Double
}