package com.ali.server.caching.configuration.properties

import jakarta.validation.constraints.Min
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties(prefix = "cache")
class ResourceCacheProperties(
    @field:Min(1)
    val expireAfterWrite: Long,
    @field:Min(1)
    val initialCapacity: Int
)