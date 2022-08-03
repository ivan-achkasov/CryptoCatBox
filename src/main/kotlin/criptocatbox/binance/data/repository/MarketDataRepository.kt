package criptocatbox.binance.data.repository

import criptocatbox.binance.data.entity.BestPrices
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class MarketDataRepository {

    private val bestPrices = ConcurrentHashMap<String, BestPrices>(1000)

    fun saveBestPrices(bestPricesDto: BestPrices) {
        bestPrices[bestPricesDto.pair] = bestPricesDto
    }

    fun getBestPrice(pair: String): BestPrices? {
        return bestPrices[pair]
    }

    fun getAllPairs(): Set<String> {
        return HashSet(bestPrices.keys().toList())
    }

}