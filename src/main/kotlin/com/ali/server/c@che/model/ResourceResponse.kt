package com.ali.server.`c@che`.model

import java.time.Instant

data class ResourceResponse(
    val id: String,
    val nameSpace: String,
    val content: Map<String, Any?>,
    val version: Long?,
    val updatedAt: Instant
)
