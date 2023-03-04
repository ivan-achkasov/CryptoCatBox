package cryptocatbox.exchange.binance.client.dto

enum class TimeInForce {
    GTC,// - Good Till Cancel
    IOC,// - Immediate or Cancel
    FOK,// - Fill or Kill
    GTX// - Good Till Crossing (Post Only)
}