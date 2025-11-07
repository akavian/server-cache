package com.ali.server.cache.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("resources")
data class Resource(

    val id: String,
    @Indexed(name = "idx_namespace")
    val nameSpace: String,
    val content: Map<String, Any?>,
    val updatedAt: Instant,
    val version: Long
) {
    @get:Id
    val key: String
        get() = "$nameSpace;$id"

    companion object {
        fun getKey(ns: String, id: String) = "$ns:$id"
    }
}

