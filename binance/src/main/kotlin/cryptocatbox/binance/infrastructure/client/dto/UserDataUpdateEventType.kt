package cryptocatbox.binance.infrastructure.client.dto

import com.fasterxml.jackson.annotation.JsonProperty

enum class UserDataUpdateEventType {
    MARGIN_CALL,
    ACCOUNT_UPDATE,
    ORDER_TRADE_UPDATE,
    ACCOUNT_CONFIG_UPDATE,
    STRATEGY_UPDATE,
    GRID_UPDATE,

    @JsonProperty("listenKeyExpired")
    LISTEN_KEY_EXPIRED
}