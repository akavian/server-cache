package com.ali.server.cache.model

import java.time.Instant

data class Resource(
    val id: Long,
    val nameSpace: String,
    val content: Map<String, Any?>,
    val updatedAt: Instant,
    val version: Long
)
