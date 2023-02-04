package criptocatbox.exchange.binance.client.dto

data class StreamDto<E>(val stream: String?, val data: E?, val result: String?)