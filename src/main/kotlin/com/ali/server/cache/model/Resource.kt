package com.ali.server.cache.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("resources")
data class Resource(

    @Indexed(name = "idx_id") val id: String,

    @Indexed(name = "idx_namespace") val nameSpace: String,

    var content: Map<String, Any?>,

    @CreatedDate val createdAt: Instant? = null,

    @LastModifiedDate var updatedAt: Instant? = null,

    @Version var version: Long? = null
) {
    @get:Id
    val key: String
        get() = "$nameSpace;$id"

    companion object {
        fun getKey(ns: String, id: String) = "$ns:$id"
        fun fromResourceRequest(id: String, nameSpace: String, resourceRequest: ResourceRequest): Resource =
            Resource(id, nameSpace, resourceRequest.content, version = resourceRequest.version)
    }
}

fun Resource.toResourceResponse(): ResourceResponse =
    ResourceResponse(this.id, this.nameSpace, this.content, this.version, this.updatedAt ?: Instant.now())

fun Resource.updateFromResourceRequest(resourceRequest: ResourceRequest): Resource = let {
    it.content = resourceRequest.content
    it.version = resourceRequest.version
    it
}
