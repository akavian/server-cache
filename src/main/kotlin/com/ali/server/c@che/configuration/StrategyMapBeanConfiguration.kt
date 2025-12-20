package com.ali.server.`c@che`.configuration

import com.ali.server.`c@che`.configuration.annotation.StrategyQualified
import com.ali.server.`c@che`.enums.ResourceStrategy
import com.ali.server.`c@che`.enums.StrategyMap
import com.ali.server.`c@che`.exception.StrategyNotFoundException
import com.ali.server.`c@che`.service.ResourceService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StrategyMapBeanConfiguration {

    @Bean
    fun strategyMap(@StrategyQualified beans: Map<String, ResourceService>): StrategyMap = mapOf(
        ResourceStrategy.CACHED to (beans[ResourceService.DIRECT_SERVICE] ?: throw StrategyNotFoundException(
            ResourceStrategy.CACHED
        )),
        ResourceStrategy.DIRECT to (beans[ResourceService.CACHED_SERVICE] ?: throw StrategyNotFoundException(
            ResourceStrategy.DIRECT
        ))
    )
}
