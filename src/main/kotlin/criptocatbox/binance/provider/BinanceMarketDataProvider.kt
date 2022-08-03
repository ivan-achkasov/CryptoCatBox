package criptocatbox.binance.provider

import criptocatbox.binance.data.repository.MarketDataRepository
import criptocatbox.binance.mapper.mapToDomain
import criptocatbox.domain.CurrencyPair
import criptocatbox.domain.PairBestPrices
import criptocatbox.domain.parsePair
import criptocatbox.provider.MarketDataProvider
import org.springframework.stereotype.Service

@Service
class BinanceMarketDataProvider(private val marketDataRepository: MarketDataRepository): MarketDataProvider {

    private val defaultPairDelimiter = '/'

    override fun getBestPrices(currencyPair: CurrencyPair): PairBestPrices? {
        val bestPrices = marketDataRepository.getBestPrice(currencyPair.toString(defaultPairDelimiter))
        if(bestPrices != null) {
            return bestPrices.mapToDomain {currencyPair}
        }
        return marketDataRepository.getBestPrice(currencyPair.invert().toString(defaultPairDelimiter))
            ?.mapToDomain { currencyPair.invert() }?.invert()
    }

    override fun getAllAllowedPairs(): Set<CurrencyPair> {
        return marketDataRepository.getAllPairs().map { parsePair(it, defaultPairDelimiter) }.toHashSet()
    }

}