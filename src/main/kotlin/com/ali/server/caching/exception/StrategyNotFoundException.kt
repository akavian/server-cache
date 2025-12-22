package com.ali.server.caching.exception

import com.ali.server.caching.enums.ResourceStrategy

class StrategyNotFoundException(strategy: ResourceStrategy) :
    IllegalStateException("Strategy $strategy is not defined")