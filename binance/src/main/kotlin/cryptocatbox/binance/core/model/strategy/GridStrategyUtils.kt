package cryptocatbox.binance.core.model.strategy

import java.math.BigDecimal

class GridStrategyUtils private constructor() {
    companion object {
        fun getNextBuyOrderPrice(currentOrderPrice: BigDecimal, settings: GridSettings): BigDecimal {
            return currentOrderPrice - (currentOrderPrice * settings.stepDistinction.scaleByPowerOfTen(-2))
        }

        fun getNextSellOrderPrice(currentOrderPrice: BigDecimal, settings: GridSettings): BigDecimal {
            return currentOrderPrice + (currentOrderPrice * settings.stepDistinction.scaleByPowerOfTen(-2))
        }
    }
}