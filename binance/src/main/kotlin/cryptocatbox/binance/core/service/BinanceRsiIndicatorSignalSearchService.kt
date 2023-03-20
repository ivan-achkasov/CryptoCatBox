package cryptocatbox.binance.core.service

//@Service
/*
class BinanceRsiIndicatorSignalSearchService(
    @Qualifier("1MinPerpetualStorage") private val oneMinStorage: CandlesHeapStorage,
    @Qualifier("5MinPerpetualStorage") private val fiveMinStorage: CandlesHeapStorage,
    @Qualifier("15MinPerpetualStorage") private val fifteenMinStorage: CandlesHeapStorage,
    @Qualifier("1HourPerpetualStorage") private val oneHourStorage: CandlesHeapStorage
) : Thread(), InitializingBean, Logging {

    private val updateFrequency = 500 //0.5sec

    private val higherLimit = 85.0
    private val lowerLimit = 15.0
    private val timeDelta = 60000 // 1min
    private val rsiDelta = 0.5

    private val depth = 19

    private data class TimedValue(val value: Double, val time: Long)

    private val timedLastValues = ConcurrentHashMap<CurrencyPair, TimedValue>(200)
    private val calculator = RsiIndicatorEmaCalculator()

    override fun afterPropertiesSet() {
        this.start()
    }

    override fun run() {
        while (!interrupted()) {
            val checkingStartTime = System.currentTimeMillis()
            val pairs = oneMinStorage.getAllActivePairs()
            for (pair in pairs) {
                val candles1MinHistory = oneMinStorage.getLastNCandlesOrNull(pair, depth) ?: continue
                val candles5MinHistory = fiveMinStorage.getLastNCandlesOrNull(pair, depth)
                val candles15MinHistory = fifteenMinStorage.getLastNCandlesOrNull(pair, depth)
                val candles1HHistory = oneHourStorage.getLastNCandlesOrNull(pair, depth)
                val rsi1Min = candles1MinHistory.let { calculator.getValue(it) }
                val rsi5Min = candles5MinHistory?.let { calculator.getValue(it) }
                val rsi15Min = candles15MinHistory?.let { calculator.getValue(it) }
                val rsi1Hour = candles1HHistory?.let { calculator.getValue(it) }
                process(pair, rsi1Min, rsi5Min, rsi15Min, rsi1Hour, candles1MinHistory.last().closePrice)
            }
            val spentTime = System.currentTimeMillis() - checkingStartTime
            if(spentTime < updateFrequency) {
                sleep(updateFrequency - spentTime)
            }
        }
    }

    fun process(
        pair: CurrencyPair,
        rsi1Min: Double,
        rsi5Min: Double?,
        rsi15Min: Double?,
        rsi1Hour: Double?,
        lastPrice: Double
    ) {
        timedLastValues.compute(pair) { _, timedValue: TimedValue? ->
            //val rsi1MinSignalConditionSatisfied = rsi1Min > 92.0 || rsi1Min < 8.0
            //val rsi5MinSignalConditionSatisfied = rsi5Min != null && (rsi5Min > 92.0 || rsi5Min < 8.0)
            val rsi15MinSignalConditionSatisfied = rsi15Min != null && (rsi15Min > 80.0 || rsi15Min < 20.0)
            val rsi1HSignalConditionSatisfied = rsi1Hour != null && (rsi1Hour > 80.0 || rsi1Hour < 20.0)

            val isSignalConditionSatisfied = //rsi1MinSignalConditionSatisfied ||
                    //rsi5MinSignalConditionSatisfied ||
                    rsi15MinSignalConditionSatisfied
                    || rsi1HSignalConditionSatisfied

            if(isSignalConditionSatisfied) {
                if (timedValue == null) {
                    doSignal(pair, rsi1Min, rsi5Min, rsi15Min, rsi1Hour, lastPrice)
                    return@compute TimedValue(0.0, System.currentTimeMillis())
                } else if (System.currentTimeMillis() - timedValue.time > timeDelta) {
                    doSignal(pair, rsi1Min, rsi5Min, rsi15Min, rsi1Hour, lastPrice)
                    return@compute TimedValue(0.0, System.currentTimeMillis())
                }
            }
            return@compute timedValue
        }
    }

    */
/*fun process(pair: CurrencyPair, rsi1Min: Double, rsi5Min: Double, rsi15Min: Double, rsi1Hour: Double, lastPrice: Double) {
        val avgRsiValue = (rsi1Min + rsi5Min + rsi15Min) / 3.0
        timedLastValues.compute(pair) { _, timedValue: TimedValue? ->
            if (timedValue == null) {
                if (areLimitsSatisfied(avgRsiValue)) {
                    doSignal(pair, rsi1Min, rsi5Min, rsi15Min, lastPrice)
                    return@compute TimedValue(avgRsiValue, System.currentTimeMillis())
                }
                return@compute null
            }

            if (System.currentTimeMillis() - timedValue.time > timeDelta) {
                if (areLimitsSatisfied(avgRsiValue)) {
                    doSignal(pair, rsi1Min, rsi5Min, rsi15Min, lastPrice)
                    return@compute TimedValue(avgRsiValue, System.currentTimeMillis())
                }
                return@compute timedValue
            }

            if (
                (avgRsiValue > higherLimit && avgRsiValue - timedValue.value > rsiDelta)
                || (avgRsiValue < lowerLimit && timedValue.value - avgRsiValue > rsiDelta)
            ) {
                doSignal(pair, rsi1Min, rsi5Min, rsi15Min, lastPrice)
                return@compute TimedValue(avgRsiValue, System.currentTimeMillis())
            }

            return@compute timedValue
        }

    }*//*


    private fun areLimitsSatisfied(rsiValue: Double): Boolean {
        return rsiValue > higherLimit || rsiValue < lowerLimit
    }

    private fun doSignal(
        pair: CurrencyPair,
        rsi1Min: Double?,
        rsi5Min: Double?,
        rsi15Min: Double?,
        rsi1h: Double?,
        lastPrice: Double
    ) {
        println("__beep__ $pair RSI 1 min: $rsi1Min, RSI 5 min: $rsi5Min, RSI 15 min: $rsi15Min, RSI 1 hour: $rsi1h")

        */
/*val quantity = 20.0 / lastPrice
        if(rsi15MinValue > 50.0) {
            //featuresService.sellWithMarketPrice(pair, quantity, lastPrice * 0.975, lastPrice * 1.015)
        } else {
            //featuresService.buyWithMarketPrice(pair, quantity, lastPrice * 1.025, lastPrice * 0.9875)
        }*//*

    }

}*/
