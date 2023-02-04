package criptocatbox.exchange.common.data.repository

import criptocatbox.domain.CurrencyPair
import criptocatbox.domain.PairBestPrices
import criptocatbox.repository.MarketDataRepository
import java.util.concurrent.ConcurrentHashMap

abstract class AbstractMarketDataHeapRepository: MarketDataRepository {
    private val bestPrices = ConcurrentHashMap<CurrencyPair, PairBestPrices>(10000)

    override fun saveBestPrices(bestPricesDto: PairBestPrices) {
        bestPrices[bestPricesDto.currencyPair] = bestPricesDto
    }

    override fun getBestPrice(pair: CurrencyPair): PairBestPrices? {
        return bestPrices[pair]
    }

    override fun getAllPairs(): Set<CurrencyPair> {
        return HashSet(bestPrices.keys().toList())
    }
}