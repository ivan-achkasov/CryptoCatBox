package cryptocatbox

class Solution {
    data class ProcessResult(val shift: Int, val increase: Int)

    fun romanToInt(s: String): Int {
        var resultInt = 0
        var i = 0
        while (i < s.length - 1) {
            val currentChar = s[i]
            val nextChar = s[i + 1]
            val processResult = processPair(currentChar, nextChar)
            i += processResult.shift
            resultInt += processResult.increase
        }
        if (i < s.length) {
            val lastProcessResult = processPair(s[s.length - 1], null)
            resultInt += lastProcessResult.increase
        }

        return resultInt
    }

    private fun processPair(curr: Char, next: Char?): ProcessResult {
        return when (curr) {
            'I' -> {
                return if (next == 'V') {
                    ProcessResult(2, 4)
                } else if (next == 'X') {
                    ProcessResult(2, 9)
                } else {
                    ProcessResult(1, 1)
                }
            }

            'X' -> {
                return when (next) {
                    'L' -> ProcessResult(2, 40)
                    'C' -> ProcessResult(2, 90)
                    else -> ProcessResult(1, 10)
                }
            }

            'C' -> {
                return when (next) {
                    'D' -> ProcessResult(2, 400)
                    'M' -> ProcessResult(2, 900)
                    else -> ProcessResult(1, 100)
                }
            }

            'V' -> return ProcessResult(1, 5)
            'L' -> return ProcessResult(1, 50)
            'D' -> return ProcessResult(1, 500)
            'M' -> return ProcessResult(1, 1000)

            else -> throw IllegalArgumentException()
        }
    }
}

fun main(args: Array<String>) {
    println(Solution().romanToInt("MCMXCIV"))
}

fun main1(args: Array<String>) {

    /*var quantity: Int = 0;
    var currentPositionPrice: Int = 0

    for (i in 25 downTo 0) {
        val currentPrice: Int = i * 1000
        println("Current price: $currentPrice")

        currentPositionPrice = ((currentPositionPrice * quantity) + currentPrice) / ++quantity;
        println("Current position price: $currentPositionPrice")

        val currentLiqPrice: Double = currentPositionPrice - (currentPositionPrice / 1.0 * 0.9);
        println("Current liquidation price: $currentLiqPrice")

        println()
    }*/

    val startPrice = 308.0
    val limitPrice = 120.0

    var currentPrice = startPrice
    var totallySpent = 0.0
    var quantity = 0.0
    var currentPositionPrice = 0.0


    while (currentPrice > limitPrice) {
        currentPrice *= 0.995
        totallySpent += 5.0
        println("Current price: $currentPrice. Now spent: $totallySpent")


        currentPositionPrice = ((currentPositionPrice * quantity) + currentPrice) / ++quantity;
        println("Current position price: $currentPositionPrice")

        val currentLiqPrice: Double = currentPositionPrice - (currentPositionPrice / 2.0 * 0.9);
        println("Current liquidation price: $currentLiqPrice")

        println("-----------")
    }
}