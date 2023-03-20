package cryptocatbox.common.core

import java.math.BigDecimal

class BigDecimalUtils private constructor() {
    companion object {
        fun avg(vararg values: BigDecimal): BigDecimal {
            var sum = BigDecimal.ZERO
            for (value in values) {
                sum += value
            }
            return sum / values.size.toBigDecimal()
        }
    }
}