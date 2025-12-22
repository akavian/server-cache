package com.ali.server.caching.enums

import com.ali.server.caching.service.ResourceService

enum class ResourceStrategy(val beanName: String) {
    CACHED(ResourceService.CACHED_SERVICE), DIRECT(ResourceService.DIRECT_SERVICE);
}

typealias StrategyMap = Map<ResourceStrategy, ResourceService>