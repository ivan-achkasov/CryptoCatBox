package cryptocatbox.exchange.binance.service

//import javax.annotation.PreDestroy

//TODO: add logging
//@Service
/*
class BinancePerpetualCandlesAutoUpdateService(
    private val futurePairsProvider: FuturePairsProvider,
    private val futuresDataProvider: FuturesDataProvider,
    private val webSocketClient: cryptocatbox.exchange.binance.client.ws.BinanceFuturesWebSocketClient,
    private val objectMapper: ObjectMapper,
    @Qualifier("1MinPerpetualStorage") private val oneMinStorage: CandlesHeapStorage,
    @Qualifier("5MinPerpetualStorage") private val fiveMinStorage: CandlesHeapStorage,
    @Qualifier("15MinPerpetualStorage") private val fifteenMinStorage: CandlesHeapStorage,
    @Qualifier("1HourPerpetualStorage") private val oneHourStorage: CandlesHeapStorage
) : InitializingBean, Logging {

    private val initDataSize = 1500
    private val allowedFiatCurrencies = setOf(Currency.USDT)
    private val ignoredCryptoCurrencies = setOf(Currency.SC, Currency.BTS)

    private val connection: BinanceDefaultWebSocketConnection = webSocketClient.openNewDefaultConnection {
        this.onNewData(it)
    }

    private val updateDataThreadPool = Executors.newSingleThreadExecutor()

    override fun afterPropertiesSet() {
        GlobalScope.launch {
            init()
        }
    }

    private suspend fun init() {
        val pairs = futurePairsProvider.getAllowedPairs()
            .filter { it.sell in allowedFiatCurrencies }
            .filter { it.buy !in ignoredCryptoCurrencies }
            .toSet()
        connection.sendMessage(cryptocatbox.exchange.binance.client.ws.FuturesWebSocketMessages.subscribePerpetual1MinCandles(pairs))
        pairs.forEach { pair ->
            val oldData = futuresDataProvider.getLast1MinCandles(pair, initDataSize.toShort())
            //candlesStorage.putEarlyData(oldData)
            oneMinStorage.putPreviousCandlesWithMerge(oldData)
            fiveMinStorage.putPreviousCandlesWithMerge(generateBiggerCandlesBasedOnNew1MinCandle(pair, oldData, 5))
            fifteenMinStorage.putPreviousCandlesWithMerge(generateBiggerCandlesBasedOnNew1MinCandle(pair, oldData, 15))
            oneHourStorage.putPreviousCandlesWithMerge(generateBiggerCandlesBasedOnNew1MinCandle(pair, oldData, 60))
        }
    }

    private fun generateBiggerCandlesBasedOnNew1MinCandle(pair: CurrencyPair,
                                                          candles: List<Candle>,
                                                          multiplier: Int): List<Candle> {
        val result = LinkedList<Candle>()
        var lastCandle: Candle? = null

        for(candle in candles) {
            lastCandle = result.lastOrNull()
            if (lastCandle == null) {
                //Ignore candle if it is not start of {multiplier} min candle
                if (candle.startTime % (multiplier * 60 * 1000) == 0L) {
                    val newCandle = Candle(
                        pair,
                        candle.startTime,
                        candle.startTime + (multiplier * 60 * 1000) - 1,
                        candle.openPrice,
                        candle.closePrice,
                        false
                    )
                    result.add(newCandle)
                }
            } else {
                if (lastCandle.closeTime >= candle.closeTime) {//TODO: add delta check (not more than 5 min)
                    val newUpdatedLastCandle = Candle(
                        pair,
                        lastCandle.startTime,
                        lastCandle.closeTime,
                        lastCandle.openPrice,
                        candle.closePrice,
                        candle.closed
                    )
                    result.pollLast()
                    result.add(newUpdatedLastCandle)
                } else if (lastCandle.closeTime + 1 == candle.startTime) {
                    if (!lastCandle.closed) {
                        throw LostDataIntegrity("Some data is missed for pair[${pair.buy}/${pair.sell}].")
                    }
                    val newCandle = Candle(
                        pair,
                        lastCandle.closeTime + 1,
                        lastCandle.closeTime + (multiplier * 60 * 1000),
                        candle.openPrice,
                        candle.closePrice,
                        false
                    )
                    result.add(newCandle)
                } else {
                    throw LostDataIntegrity("Some data is missed for pair[${pair.buy}/${pair.sell}].")
                }
            }
        }
        return result
    }

    private fun insertNewDataToStorages(new1MinCandle: Candle) {

        //if((new1MinCandle.pair.buy == Currency.BTC && (new1MinCandle.closePrice < 16350.0 || new1MinCandle.closePrice > 18000.0))) {
        //    println("__beep__ ${new1MinCandle.pair} ___________________")
        //}


        val pair = new1MinCandle.pair
        oneMinStorage.putNewCandle(new1MinCandle)

        generateAndPutNewXCandleBasedOnNew1MinCandle(pair, fiveMinStorage, new1MinCandle, 5)
        generateAndPutNewXCandleBasedOnNew1MinCandle(pair, fifteenMinStorage, new1MinCandle, 15)
        generateAndPutNewXCandleBasedOnNew1MinCandle(pair, oneHourStorage, new1MinCandle, 60)
    }

    private fun generateAndPutNewXCandleBasedOnNew1MinCandle(pair: CurrencyPair,
                                                             xMinStorage: CandlesHeapStorage,
                                                             new1MinCandle: Candle,
                                                             multiplier: Int) {
        val lastCandle = xMinStorage.getLastCandleOrNull(pair)
        if(lastCandle == null) {
            //Ignore candle if it is not start of {multiplier} min candle
            if (new1MinCandle.startTime % (multiplier * 60 * 1000) == 0L) {
                val newCandle = Candle(
                    pair,
                    new1MinCandle.startTime,
                    new1MinCandle.startTime + (multiplier * 60 * 1000) - 1,
                    new1MinCandle.openPrice,
                    new1MinCandle.closePrice,
                    false
                )
                xMinStorage.putNewCandle(newCandle)
            }
        } else {
            if (lastCandle.closeTime >= new1MinCandle.closeTime) {//TODO: add delta check (not more than 5 min)
                val newUpdatedLastCandle = Candle(
                    pair,
                    lastCandle.startTime,
                    lastCandle.closeTime,
                    lastCandle.openPrice,
                    new1MinCandle.closePrice,
                    lastCandle.closeTime == new1MinCandle.closeTime && new1MinCandle.closed
                )
                xMinStorage.putNewCandle(newUpdatedLastCandle)
            } else if (lastCandle.closeTime + 1 == new1MinCandle.startTime) {
                if (!lastCandle.closed) {
                    throw LostDataIntegrity("Some data is missed for pair[${pair.buy}/${pair.sell}].")
                }
                val newCandle = Candle(
                    pair,
                    lastCandle.closeTime + 1,
                    lastCandle.closeTime + (multiplier * 60 * 1000),
                    new1MinCandle.openPrice,
                    new1MinCandle.closePrice,
                    false
                )
                xMinStorage.putNewCandle(newCandle)
            } else {
                throw LostDataIntegrity("Some data is missed for pair[${pair.buy}/${pair.sell}].")
            }
        }
    }

    private fun onNewData(rawData: String) {
        updateDataThreadPool.execute {
            rawData.readCandleValue()?.let {
                try {
                    insertNewDataToStorages(it)
                } catch (ex: Exception) {
                    logger().error("Error during inserting new candle", ex)
                    connection.sendMessage(cryptocatbox.exchange.binance.client.ws.FuturesWebSocketMessages.unsubscribePerpetual1MinCandles(it.pair))
                }
            }
        }
    }

    private fun String.readCandleValue(): Candle? {
        if (this.contains("\"result\":")) {
            return null
        }
        val dto = objectMapper.readValue(this, object : TypeReference<cryptocatbox.exchange.binance.client.ws.dto.KLineDto>() {})
        if (dto == null) {
            return null
        }
        val pair = parsePairWithoutDelimiter(dto.symbol)
        return dto.toDomainModel(pair)
    }

    @PreDestroy
    fun preDestroy() {
        connection.close()
        updateDataThreadPool.shutdown()
    }
}*/
