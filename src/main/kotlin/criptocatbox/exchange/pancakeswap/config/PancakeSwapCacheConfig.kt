package criptocatbox.exchange.pancakeswap.config

import org.springframework.cache.annotation.CacheEvict
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled

@Configuration
class PancakeSwapCacheConfig() {
    @CacheEvict(allEntries = true, cacheNames = ["pancake_top_pairs"])
    @Scheduled(fixedDelay = 5000)
    fun cacheEvict() {
    }
}