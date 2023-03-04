package cryptocatbox.service.impl

import cryptocatbox.common.domain.Currency
import cryptocatbox.common.domain.CurrencyPair
import cryptocatbox.domain.SingleExchangeChain
import cryptocatbox.service.arbitrage.ChainsCreator
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class ChainsCreatorTest {
    private val chainsCreator = ChainsCreator()

    @Test
    fun `should return empty collection by provided pairs without chains`() {
        val allowedPairs = setOf(
            CurrencyPair(Currency.BTC, Currency.USDT),
            CurrencyPair(Currency.USDT, Currency.ETH),
            CurrencyPair(Currency.USDT, Currency.CAKE),
            CurrencyPair(Currency.DOGE, Currency.CAKE),
            CurrencyPair(Currency.DOGE, Currency.BTC),
        )

        val resultChains = chainsCreator.createSingleExchangeChains(allowedPairs)

        assertEquals(emptySet<SingleExchangeChain>(), resultChains)
    }

    @Test
    fun `should create arbitrage chains by provided pairs`() {
        val allowedPairs = setOf(
            CurrencyPair(Currency.BTC, Currency.USDT),
            CurrencyPair(Currency.BTC, Currency.ETH),
            CurrencyPair(Currency.USDT, Currency.ETH),
            CurrencyPair(Currency.CAKE, Currency.ETH),
            CurrencyPair(Currency.DOGE, Currency.ETH),
        )

        val resultChains = chainsCreator.createSingleExchangeChains(allowedPairs)

        assertEquals(
            setOf(
                SingleExchangeChain(Currency.BTC, Currency.USDT, Currency.ETH),
                SingleExchangeChain(Currency.ETH, Currency.USDT, Currency.BTC)
            ), resultChains
        )
    }
}