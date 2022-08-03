package criptocatbox.binance.service

import com.binance.connector.client.impl.WebsocketClientImpl
import com.fasterxml.jackson.databind.ObjectMapper
import criptocatbox.Logging
import criptocatbox.binance.data.repository.MarketDataRepository
import criptocatbox.binance.dto.BestPricesDto
import criptocatbox.binance.mapper.mapToDataEntity
import criptocatbox.logger
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Service
import javax.annotation.PreDestroy


@Service
class DataAutoUpdateService(
    private val marketDataRepository: MarketDataRepository,
    private val objectMapper: ObjectMapper
) : InitializingBean, Logging {

    private val defaultDelimiter = '/'

    private val client = WebsocketClientImpl()

    var allTickersStream: Int = -1

    override fun afterPropertiesSet() {
        recreateAllTickersStream()
    }

    @PreDestroy
    fun beforeDestroy() {
        client.closeAllConnections()
    }

    private fun recreateAllTickersStream() {
        synchronized(client) {
            client.closeConnection(allTickersStream)
            val onError: (_: String) -> Unit = {
                logger().warn("Connection for stream 'allBookTickerStream' is failed. Recreating it.")
                recreateAllTickersStream()
            }
            allTickersStream = client.allBookTickerStream({}, this::updateBestPrices, {}, onError)
        }
    }

    private fun updateBestPrices(rawBestPrices: String) {
        val bestPrices = objectMapper.readValue(rawBestPrices, BestPricesDto::class.java)
        marketDataRepository.saveBestPrices(bestPrices.mapToDataEntity(defaultDelimiter))
    }
}