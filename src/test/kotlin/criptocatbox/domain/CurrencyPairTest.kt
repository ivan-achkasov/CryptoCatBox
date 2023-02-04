package criptocatbox.domain

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class CurrencyPairTest {

    @Test
    fun `should have the same hash for inverted pair`() {
        val pair = CurrencyPair(Currency.CAKE, Currency.ETH)
        val invertedPair = pair.invert()

        Assertions.assertEquals(pair.hashCode(), invertedPair.hashCode())
    }

    @Test
    fun `should be equal for inverted pair`() {
        val pair = CurrencyPair(Currency.CAKE, Currency.ETH)
        val invertedPair = pair.invert()

        Assertions.assertEquals(pair, invertedPair)
    }

}