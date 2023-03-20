package cryptocatbox.binance.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories

@Configuration
@ComponentScan("cryptocatbox.binance")
@EnableJdbcRepositories(basePackages = ["cryptocatbox.binance.infrastructure.persistence"])
open class BinanceConfiguration {
}