package com.ali.server.`c@che`.exception

import com.ali.server.`c@che`.enums.ResourceStrategy

class StrategyNotFoundException(strategy: ResourceStrategy) :
    IllegalStateException("Strategy $strategy is not defined")