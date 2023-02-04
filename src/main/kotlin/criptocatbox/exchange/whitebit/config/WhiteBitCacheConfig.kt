package criptocatbox.exchange.whitebit.config

import criptocatbox.Logging
import criptocatbox.logger
import org.springframework.cache.annotation.CacheEvict
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled

@Configuration
class WhiteBitCacheConfig: Logging {

    @CacheEvict(allEntries = true, cacheNames = ["allowed_pairs", "tickers"])
    @Scheduled(fixedDelay = 5000)
    fun cacheEvict() {
        logger().debug("Cleaning cache for WhiteBit")
    }

}