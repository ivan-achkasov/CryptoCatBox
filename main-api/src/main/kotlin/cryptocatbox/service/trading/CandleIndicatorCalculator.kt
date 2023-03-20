package cryptocatbox.service.trading

import cryptocatbox.common.domain.Candle

interface CandleIndicatorCalculator {
    fun getValue(candles: List<Candle>): Double
}