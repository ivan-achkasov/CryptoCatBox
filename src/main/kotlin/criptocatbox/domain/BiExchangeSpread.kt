package criptocatbox.domain

data class BiExchangeSpread(val buy: ArbitrageNode, val sell: ArbitrageNode, val spread: Double)
