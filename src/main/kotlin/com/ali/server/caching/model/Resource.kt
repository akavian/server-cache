package com.ali.server.caching.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

private const val dummy = "dummy"

@Document("resources")
class Resource private constructor(

    @Id val key: String,

    @Indexed(name = "idx_id") val docId: String,

    @Indexed(name = "idx_namespace") val nameSpace: String,

    var content: Map<String, Any?>,

    @CreatedDate val createdAt: Instant? = null,

    @LastModifiedDate var updatedAt: Instant? = null,

    @Version var version: Long? = null
) {
    companion object {
        fun getKey(ns: String, id: String) = "$ns:$id"
        fun create(
            docId: String,
            nameSpace: String,
            content: Map<String, Any?>,
            createdAt: Instant? = null,
            updatedAt: Instant? = null,
            version: Long? = null
        ) =
            Resource("$nameSpace:$docId", docId, nameSpace, content, createdAt, updatedAt, version)

        fun fromResourceRequest(docId: String, nameSpace: String, resourceRequest: ResourceRequest) =
            Resource("$nameSpace:$docId", docId, nameSpace, resourceRequest.content, version = resourceRequest.version)

        fun fromResourceQueryExample(resourceQueryExample: ResourceQueryExample): Pair<Resource, List<String>> {

            val ignoredPaths = buildList {
                addAll(listOf("createdAt", "updatedAt", "content"))
                if (resourceQueryExample.docId == null && resourceQueryExample.nameSpace == null) add("key")
                if (resourceQueryExample.docId == null) add("docId")
                if (resourceQueryExample.nameSpace == null) add("nameSpace")
                if (resourceQueryExample.version == null) add("version")
            }

            return Pair(
                create(
                    resourceQueryExample.docId ?: dummy,
                    resourceQueryExample.nameSpace ?: dummy,
                    mapOf(),
                    version = resourceQueryExample.version
                ),
                ignoredPaths
            )
        }
    }
}

fun Resource.toResourceResponse(): ResourceResponse =
    ResourceResponse(this.docId, this.nameSpace, this.content, this.version, this.updatedAt ?: Instant.now())

fun Resource.updateFromResourceRequest(resourceRequest: ResourceRequest): Resource = let {
    it.content = resourceRequest.content
    it.version = resourceRequest.version
    it
}
