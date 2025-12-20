package com.ali.server.cache.configuration.annotation

import org.springframework.beans.factory.annotation.Qualifier

@Qualifier("strategy")
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.CLASS)
annotation class StrategyQualified
