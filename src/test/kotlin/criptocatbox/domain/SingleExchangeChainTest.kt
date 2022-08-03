package criptocatbox.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

internal class SingleExchangeChainTest {

    @Test
    fun `should return the same hash code for three variants of the same chain`() {
        val chain1 = SingleExchangeChain(Currency.BTC, Currency.USDT, Currency.ETH)
        val chain2 = SingleExchangeChain(Currency.USDT, Currency.ETH, Currency.BTC)
        val chain3 = SingleExchangeChain(Currency.ETH, Currency.BTC, Currency.USDT)

        assertEquals(chain1.hashCode(), chain2.hashCode())
        assertEquals(chain2.hashCode(), chain3.hashCode())
        assertEquals(chain3.hashCode(), chain1.hashCode())
    }

    @Test
    fun `should return true for equals() method for three variants of the same chain`() {
        val chain1 = SingleExchangeChain(Currency.BTC, Currency.USDT, Currency.ETH)
        val chain2 = SingleExchangeChain(Currency.USDT, Currency.ETH, Currency.BTC)
        val chain3 = SingleExchangeChain(Currency.ETH, Currency.BTC, Currency.USDT)


        assertEquals(chain1, chain2)
        assertEquals(chain2, chain3)
        assertEquals(chain3, chain1)
    }

    @Test
    fun `should return false for equals() method for inverted chain`() {
        val chain1 = SingleExchangeChain(Currency.BTC, Currency.USDT, Currency.ETH)
        val chain2 = SingleExchangeChain(Currency.ETH, Currency.USDT, Currency.BTC)

        assertNotEquals(chain1, chain2)
    }
}