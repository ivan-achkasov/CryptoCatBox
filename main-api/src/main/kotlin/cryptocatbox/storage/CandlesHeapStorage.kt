package cryptocatbox.storage

import cryptocatbox.common.domain.Candle
import cryptocatbox.common.domain.CurrencyPair
import java.util.LinkedList
import java.util.concurrent.ConcurrentHashMap

//TODO: remove all synchronized blocks in this class
class CandlesHeapStorage(private val historyLimit: Int) {
    private val storage = ConcurrentHashMap<CurrencyPair, LinkedList<Candle>>()

    fun getAllActivePairs(): List<CurrencyPair> {//TODO: should be removed or reviewed
        return storage.keys().toList()
    }

    fun putPreviousCandlesWithMerge(oldCandles: List<Candle>) {
        if (oldCandles.isEmpty()) {
            return
        }
        val pair = oldCandles.first().pair

        val pairHistory = storage.computeIfAbsent(pair) { LinkedList<Candle>() }

        oldCandles.forEach {
            if (it.pair != pair) {
                throw RuntimeException("Not allowed pair")//TODO: do custom exception
            }
            //TODO: check if current el starts right after prev
        }

        synchronized(pairHistory) {
            if (pairHistory.isEmpty()) {
                pairHistory.addAll(oldCandles)//TODO:add checks
            } else {

                val oldestHistoryCandle = pairHistory.first()
                val newestOldDataCandle = oldCandles.last()

                if (newestOldDataCandle.closeTime + 1 < oldestHistoryCandle.startTime) {
                    throw LostDataIntegrity("Old data is too old. Pair: $pair")
                }

                val dataToAdd = oldCandles.dropLastWhile { oldestHistoryCandle.startTime < it.closeTime }

                dataToAdd.reversed().forEach {
                    putPreviousCandle(it)
                }
            }
        }
    }

    fun putNewCandle(newCandle: Candle) {
        val pair = newCandle.pair
        val pairHistory = storage.computeIfAbsent(pair) { LinkedList<Candle>() }
        synchronized(pairHistory) {
            if (pairHistory.isNotEmpty()) {
                val lastClosed = pairHistory.lastOrNull { it.closed }
                if (lastClosed != null && newCandle.startTime != lastClosed.closeTime + 1) {
                    throw LostDataIntegrity("Some data is missed for pair[${pair.buy}/${pair.sell}].")
                }
            }

            if (pairHistory.lastOrNull()?.closeTime == newCandle.closeTime) {
                pairHistory.pollLast()
            }

            pairHistory.add(newCandle)

            if (pairHistory.size > historyLimit) {
                pairHistory.pollFirst()
            }
        }
    }

    fun getLastNCandlesOrNull(pair: CurrencyPair, size: Int): List<Candle>? {
        val history = storage[pair] ?: return null

        synchronized(history) {
            val lastNElements = history.takeLast(size)
            if (lastNElements.size < size) {
                return null
            }
            return lastNElements
        }
    }

    fun getLastCandleOrNull(pair: CurrencyPair): Candle? {
        val history = storage[pair] ?: return null

        synchronized(history) {
            return history.lastOrNull()
        }
    }

    private fun putPreviousCandle(prevCandle: Candle) {
        val history = storage.computeIfAbsent(prevCandle.pair) { LinkedList<Candle>() }
        val first = history.first
        if(prevCandle.closeTime + 1 != first.startTime) {
            throw LostDataIntegrity("Some data is missed for pair[${prevCandle.pair.buy}/${prevCandle.pair.sell}].")
        }
        history.addFirst(prevCandle)
    }
}

class LostDataIntegrity(message: String?) : RuntimeException(message)