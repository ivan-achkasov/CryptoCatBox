package criptocatbox.exchange.binance.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import criptocatbox.Logging
import criptocatbox.data.storage.CandlesHeapStorage
import criptocatbox.data.storage.LostDataIntegrity
import criptocatbox.domain.Candle
import criptocatbox.domain.Currency
import criptocatbox.domain.CurrencyPair
import criptocatbox.domain.parsePairWithoutDelimiter
import criptocatbox.exchange.binance.client.BinanceDefaultWebSocketConnection
import criptocatbox.exchange.binance.client.BinanceFuturesWebSocketClient
import criptocatbox.exchange.binance.client.FuturesWebSocketConnections
import criptocatbox.exchange.binance.client.dto.KLineDto
import criptocatbox.exchange.binance.converter.toDomainModel
import criptocatbox.logger
import criptocatbox.provider.FuturePairsProvider
import criptocatbox.provider.FuturesDataProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.util.LinkedList
import java.util.concurrent.Executors
import javax.annotation.PreDestroy

//TODO: add logging
@Service
class BinancePerpetualCandlesAutoUpdateService(
    private val futurePairsProvider: FuturePairsProvider,
    private val futuresDataProvider: FuturesDataProvider,
    private val webSocketClient: BinanceFuturesWebSocketClient,
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
        connection.sendMessage(FuturesWebSocketConnections.subscribePerpetual1MinCandles(pairs))
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
                    connection.sendMessage(FuturesWebSocketConnections.unsubscribePerpetual1MinCandles(it.pair))
                }
            }
        }
    }

    private fun String.readCandleValue(): Candle? {
        if (this.contains("\"result\":")) {
            return null
        }
        val dto = objectMapper.readValue(this, object : TypeReference<KLineDto>() {})
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
}