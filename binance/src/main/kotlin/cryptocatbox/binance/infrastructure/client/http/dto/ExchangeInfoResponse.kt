package cryptocatbox.binance.infrastructure.client.http.dto

data class ExchangeInfoResponse(
    val symbols: List<Symbol>
) {
    data class Symbol (
        val baseAsset: String,
        val marginAsset: String,
        val pricePrecision: Int,
        val quantityPrecision: Int,
        val filters: Set<Filter>
    )

    data class Filter (
        val filterType: FilterType,
        val tickSize: Double?,
        val stepSize: Double?
    )

    enum class FilterType {
        PRICE_FILTER,
        LOT_SIZE,
        MARKET_LOT_SIZE,
        MAX_NUM_ORDERS,
        MAX_NUM_ALGO_ORDERS,
        MIN_NOTIONAL,
        PERCENT_PRICE
    }
}