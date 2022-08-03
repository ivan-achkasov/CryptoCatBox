package criptocatbox.service.impl

import criptocatbox.domain.Currency
import criptocatbox.domain.CurrencyPair
import criptocatbox.domain.SingleExchangeChain
import org.springframework.stereotype.Component

@Component
class ChainsCreator {

    fun createSingleExchangeChains(allowedPairs: Set<CurrencyPair>): Set<SingleExchangeChain> {
        val pairsByKeyCurrency = HashMap<Currency, HashSet<Currency>>()

        allowedPairs.forEach {
            addNewPair(pairsByKeyCurrency, it)
            addNewPair(pairsByKeyCurrency, it.invert())
        }

        val result = HashSet<SingleExchangeChain>()
        val firstLevelPairsIterator = pairsByKeyCurrency.iterator()
        while (firstLevelPairsIterator.hasNext()) {
            val firstCoinPairs = firstLevelPairsIterator.next()
            val mainCoin = firstCoinPairs.key
            val secondCoins = firstCoinPairs.value

            val secondLevelPairsIterator = secondCoins.iterator()
            while (secondLevelPairsIterator.hasNext()) {
                val secondCoin = secondLevelPairsIterator.next()
                val allCoinsPairsForSecondCoin = pairsByKeyCurrency[secondCoin]
                for (thirdCoin in allCoinsPairsForSecondCoin ?: emptySet()) {
                    if (secondCoins.contains(thirdCoin)) {
                        result.add(SingleExchangeChain(mainCoin, secondCoin, thirdCoin))
                        result.add(SingleExchangeChain(thirdCoin, secondCoin, mainCoin))
                    }
                }
                secondLevelPairsIterator.remove()
            }
            firstLevelPairsIterator.remove()
        }
        return result
    }

    private fun addNewPair(pairsByKeyCurrency: HashMap<Currency, HashSet<Currency>>, pair: CurrencyPair) {
        var allowedCurrencies = pairsByKeyCurrency[pair.buy]
        if (allowedCurrencies == null) {
            allowedCurrencies = HashSet()
            pairsByKeyCurrency[pair.buy] = allowedCurrencies
        }
        allowedCurrencies.add(pair.sale)
    }

}