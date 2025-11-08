package com.ali.server.cache.configuration

import com.ali.server.cache.enums.ResourceStrategy
import com.ali.server.cache.enums.StrategyMap
import com.ali.server.cache.exception.StrategyNotFoundException
import com.ali.server.cache.service.ResourceService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StrategyMapBeanConfiguration {

    @Bean
    fun strategyMap(beans: Map<String, ResourceService>): StrategyMap = mapOf(
        ResourceStrategy.CACHED to (beans[ResourceService.DIRECT_SERVICE] ?: throw StrategyNotFoundException(
            ResourceStrategy.CACHED
        )),
        ResourceStrategy.DIRECT to (beans[ResourceService.CACHED_SERVICE] ?: throw StrategyNotFoundException(
            ResourceStrategy.DIRECT
        ))
    )
}
