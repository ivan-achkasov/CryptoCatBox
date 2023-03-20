package cryptocatbox.binance.infrastructure.client.dto

enum class OrderType {
    LIMIT,
    MARKET,
    TAKE_PROFIT,
    TAKE_PROFIT_MARKET,
    STOP_MARKET;
}