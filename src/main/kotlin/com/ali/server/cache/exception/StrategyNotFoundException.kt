package com.ali.server.cache.exception

import com.ali.server.cache.enums.ResourceStrategy

class StrategyNotFoundException(strategy: ResourceStrategy) :
    IllegalStateException("Strategy $strategy is not defined")