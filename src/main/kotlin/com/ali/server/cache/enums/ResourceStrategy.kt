package com.ali.server.cache.enums

import com.ali.server.cache.service.ResourceService

enum class ResourceStrategy(val beanName: String) {
    CACHED(ResourceService.CACHED_SERVICE), DIRECT(ResourceService.DIRECT_SERVICE);
}

typealias StrategyMap = Map<ResourceStrategy, ResourceService>