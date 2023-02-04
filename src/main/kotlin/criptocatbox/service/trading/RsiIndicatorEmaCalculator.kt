package criptocatbox.service.trading

import criptocatbox.domain.Candle
import java.util.LinkedList

class RsiIndicatorEmaCalculator: BaseRsiIndicatorCalculator() {

    override fun calculateRelativeStrength(candles: List<Candle>): Double {
        val depth = candles.size
        val upwardChanges: LinkedList<Double> = getUpwardChanges(candles)
        val downwardChanges: LinkedList<Double> = getDownwardChanges(candles)

        val upwardMovingAverage: Double = getMovingAverage(upwardChanges, depth)
        val downwardMovingAverage: Double = getMovingAverage(downwardChanges, depth)

        return upwardMovingAverage / downwardMovingAverage
    }

    private fun getUpwardChanges(candles: List<Candle>): LinkedList<Double> {
        val result = LinkedList<Double>()

        var lastClosePrice = candles.first().openPrice
        for(candle in candles) {
            val priceDifference = candle.closePrice - lastClosePrice
            if(priceDifference > 0.0) {
                result.add(priceDifference)
            }
            lastClosePrice = candle.closePrice
        }

        return result
    }

    private fun getDownwardChanges(candles: List<Candle>): LinkedList<Double> {
        val result = LinkedList<Double>()

        var lastClosePrice = candles.first().openPrice
        for(candle in candles) {
            val priceDifference =  lastClosePrice - candle.closePrice
            if(priceDifference > 0.0) {
                result.add(priceDifference)
            }
            lastClosePrice = candle.closePrice
        }

        return result
    }

    private fun getMovingAverage(priceDifferences: List<Double>, depth: Int): Double {
        val alpha = 1.0 / depth.toDouble()


        var movingAverage = priceDifferences.sumOf { it } / depth.toDouble()
        for(priceDifference in priceDifferences) {
            movingAverage = (alpha * priceDifference + ((1.0 - alpha) * movingAverage))
        }

        return movingAverage
    }
}