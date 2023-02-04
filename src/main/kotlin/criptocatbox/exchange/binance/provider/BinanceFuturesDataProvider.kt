package criptocatbox.exchange.binance.provider

import criptocatbox.domain.Candle
import criptocatbox.domain.CurrencyPair
import criptocatbox.exchange.binance.client.BinanceFuturesHttpClient
import criptocatbox.provider.FuturesDataProvider
import org.springframework.stereotype.Service

@Service
class BinanceFuturesDataProvider(private val futuresHttpClient: BinanceFuturesHttpClient) : FuturesDataProvider {
    override fun getLast1MinCandles(pair: CurrencyPair, limit: Short): List<Candle> {
        return futuresHttpClient.getContinuousKlines("${pair.buy}${pair.sell}".lowercase(), "1m", limit).map {
            Candle(pair, it.startTime, it.closeTime, it.openPrice, it.closePrice, it.closed)
        }
    }

    override fun getCandles(pair: CurrencyPair, interval: String, startTime: Long, endTime: Long?): List<Candle> {
        return futuresHttpClient.getContinuousKlines(
            "${pair.buy}${pair.sell}".lowercase(),
            interval,
            1500,
            startTime = startTime,
            endTime = endTime
        ).map {
            Candle(pair, it.startTime, it.closeTime, it.openPrice, it.closePrice, it.closed)
        }
    }

}