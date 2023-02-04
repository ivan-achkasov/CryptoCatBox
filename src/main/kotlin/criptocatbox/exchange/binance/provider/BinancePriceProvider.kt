package criptocatbox.exchange.binance.provider

import criptocatbox.domain.CurrencyPair
import criptocatbox.domain.PairPrice
import criptocatbox.exchange.binance.data.repository.BinanceMarketDataRepository
import criptocatbox.provider.PriceProvider
import org.springframework.stereotype.Service

@Service
class BinancePriceProvider(private val marketDataRepository: BinanceMarketDataRepository): PriceProvider {
    override fun getPrice(currencyPair: CurrencyPair): PairPrice? {
        return marketDataRepository.getBestPrice(currencyPair)?.let { PairPrice(currencyPair, (it.bidPrice + it.askPrice) / 2.0) }
    }
}