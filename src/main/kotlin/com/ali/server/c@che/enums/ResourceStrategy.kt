package com.ali.server.`c@che`.enums

import com.ali.server.`c@che`.service.ResourceService

enum class ResourceStrategy(val beanName: String) {
    CACHED(ResourceService.CACHED_SERVICE), DIRECT(ResourceService.DIRECT_SERVICE);
}

typealias StrategyMap = Map<ResourceStrategy, ResourceService>