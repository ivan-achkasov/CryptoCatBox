package cryptocatbox.domain

import cryptocatbox.common.domain.Currency

data class ArbitrageNode(val exchange: Exchange, val currency: Currency)