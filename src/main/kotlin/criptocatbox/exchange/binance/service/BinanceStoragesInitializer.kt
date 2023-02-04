package criptocatbox.exchange.binance.service

import criptocatbox.data.storage.CandlesHeapStorage
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class BinanceStoragesInitializer {

    private val historyLimit: Int = 2000

    @Bean("1MinPerpetualStorage")
    fun oneMinPerpetualStorage(): CandlesHeapStorage {
        return CandlesHeapStorage(historyLimit)
    }

    @Bean("5MinPerpetualStorage")
    fun fiveMinPerpetualStorage(): CandlesHeapStorage {
        return CandlesHeapStorage(historyLimit)
    }

    @Bean("15MinPerpetualStorage")
    fun fifteenMinPerpetualStorage(): CandlesHeapStorage {
        return CandlesHeapStorage(historyLimit)
    }

    @Bean("1HourPerpetualStorage")
    fun oneHourPerpetualStorage(): CandlesHeapStorage {
        return CandlesHeapStorage(historyLimit)
    }

    @Bean("4HoursPerpetualStorage")
    fun fourHoursPerpetualStorage(): CandlesHeapStorage {
        return CandlesHeapStorage(historyLimit)
    }
}