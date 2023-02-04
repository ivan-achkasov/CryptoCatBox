package criptocatbox.exchange.whitebit.provider

import criptocatbox.domain.CurrencyPair
import criptocatbox.domain.parsePair
import criptocatbox.exchange.whitebit.client.WhiteBitHttpClient
import criptocatbox.provider.PairsProvider
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class WhiteBitPairsProvider(private val httpClient: WhiteBitHttpClient): PairsProvider {
    companion object {
        private const val DEFAULT_PAIRS_DELIMITER = '_'
    }

    @Cacheable("allowed_pairs")
    override fun getAllowedPairs(): Set<CurrencyPair> {
        val pairs = mutableSetOf<CurrencyPair>()
        httpClient.getTickers().pairsData.keys.forEach{
            try {
                pairs.add(parsePair(it, DEFAULT_PAIRS_DELIMITER))
            } catch (e: Exception) {

            }
        }
        return pairs
    }
}