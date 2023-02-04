package criptocatbox.exchange.binance.provider

import criptocatbox.domain.CurrencyPair
import criptocatbox.exchange.binance.data.repository.BinanceMarketDataRepository
import criptocatbox.provider.PairsProvider
import org.springframework.stereotype.Service

@Service
class BinancePairsProvider(private val repository: BinanceMarketDataRepository): PairsProvider {
    override fun getAllowedPairs(): Set<CurrencyPair> {
        return repository.getAllPairs()
    }
}