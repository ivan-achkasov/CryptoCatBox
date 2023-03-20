package cryptocatbox.binance.infrastructure.client.dto

enum class OrderStatus {
    NEW,
    PARTIALLY_FILLED,
    FILLED,
    CANCELED,
    EXPIRED
}