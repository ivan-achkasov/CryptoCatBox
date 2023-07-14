package cryptocatbox.binance.core.model.strategy

import java.math.BigDecimal

class GridStrategyUtils private constructor() {
    companion object {
        fun getNextBuyOrderPrice(currentOrderPrice: BigDecimal, stepDistinction: BigDecimal): BigDecimal {
            return currentOrderPrice * (BigDecimal.ONE - stepDistinction.scaleByPowerOfTen(-2))
        }

        fun getPrevBuyOrderPrice(currentOrderPrice: BigDecimal, stepDistinction: BigDecimal): BigDecimal {
            return currentOrderPrice / (BigDecimal.ONE - stepDistinction.scaleByPowerOfTen(-2))
        }

        fun getNextSellOrderPrice(currentOrderPrice: BigDecimal, stepDistinction: BigDecimal): BigDecimal {
            return currentOrderPrice * (BigDecimal.ONE + stepDistinction.scaleByPowerOfTen(-2))
        }

        fun getPrevSellOrderPrice(currentOrderPrice: BigDecimal, stepDistinction: BigDecimal): BigDecimal {
            return currentOrderPrice / (BigDecimal.ONE + stepDistinction.scaleByPowerOfTen(-2))
        }
    }
}