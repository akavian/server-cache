package com.ali.server.cache.event

sealed class ResourceEvent(open val id: String)
data class UpdateResourceEvent(override val id: String) : ResourceEvent(id)
data class DeleteResourceEvent(override val id: String) : ResourceEvent(id)