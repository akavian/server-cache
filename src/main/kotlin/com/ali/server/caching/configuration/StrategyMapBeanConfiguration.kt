package com.ali.server.caching.configuration

import com.ali.server.caching.configuration.annotation.StrategyQualified
import com.ali.server.caching.enums.ResourceStrategy
import com.ali.server.caching.enums.StrategyMap
import com.ali.server.caching.exception.StrategyNotFoundException
import com.ali.server.caching.service.ResourceService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StrategyMapBeanConfiguration {

    @Bean
    fun strategyMap(@StrategyQualified beans: Map<String, ResourceService>): StrategyMap = mapOf(
        ResourceStrategy.CACHED to (beans[ResourceService.CACHED_SERVICE] ?: throw StrategyNotFoundException(
            ResourceStrategy.CACHED
        )),
        ResourceStrategy.DIRECT to (beans[ResourceService.DIRECT_SERVICE] ?: throw StrategyNotFoundException(
            ResourceStrategy.DIRECT
        ))
    )
}
