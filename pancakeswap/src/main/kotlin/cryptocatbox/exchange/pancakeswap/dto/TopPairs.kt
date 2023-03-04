package cryptocatbox.exchange.pancakeswap.dto

import com.fasterxml.jackson.annotation.JsonAlias

data class TopPairs(
    @JsonAlias("updated_at")
    val updatedAt: Long,
    val data: Map<String, PairData>
)
