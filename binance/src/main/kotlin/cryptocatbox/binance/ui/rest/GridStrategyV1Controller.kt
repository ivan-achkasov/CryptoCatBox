package cryptocatbox.binance.ui.rest

import cryptocatbox.binance.core.model.strategy.GridSettings
import cryptocatbox.binance.core.service.strategy.grid.GridStrategyService
import cryptocatbox.binance.ui.rest.dto.CreateGridStrategyRequest
import cryptocatbox.common.domain.parsePairWithoutDelimiter
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/binance/v1/grid-strategy")
class GridStrategyV1Controller(
    private val gridStrategyService: GridStrategyService
) {

    @PostMapping
    fun createGridStrategy(@RequestBody request: CreateGridStrategyRequest): ResponseEntity<Boolean> {
        val pair = parsePairWithoutDelimiter(request.symbol)
        val gridSettings =
            GridSettings(
                pair,
                request.stepMultiplier,
                request.sellOrderQuantity,
                request.buyOrderQuantity,
                request.numOfOpenOrders
            )
        gridStrategyService.startNew(gridSettings)
        return ResponseEntity.ok(true)
    }
}