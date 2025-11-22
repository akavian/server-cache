package com.ali.server.cache.service.impl

import com.ali.server.cache.model.Resource
import com.ali.server.cache.model.ResourceRequest
import com.ali.server.cache.model.ResourceResponse
import com.ali.server.cache.model.toResourceResponse
import com.ali.server.cache.service.ResourceService
import com.github.benmanes.caffeine.cache.LoadingCache
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service(ResourceService.CACHED_SERVICE)
class CachedResourceService(private val resourceCache: LoadingCache<String, Resource>) : ResourceService {

    companion object {
        private const val PUT_NOT_IMPLEMENTED = "Saving resource in cache directly is not supported"
        private const val DELETE_NOT_IMPLEMENTED = "Deleting resource in cache directly is not supported"
    }

    override fun getResource(nameSpace: String, id: String): ResourceResponse {
        return resourceCache[Resource.getKey(nameSpace, id)].toResourceResponse()
    }

    override fun getManyResourcesInNameSpace(nameSpace: String, ids: List<String>): List<ResourceResponse> {
        return resourceCache.getAll(ids.map { Resource.getKey(nameSpace, it) })
            .map { it.value.toResourceResponse() }
            .toList()
    }

    override fun putResource(
        id: String,
        nameSpace: String,
        resourceRequest: ResourceRequest
    ) {
        throw ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, PUT_NOT_IMPLEMENTED)
    }

    override fun deleteResource(nameSpace: String, id: String) {
        throw ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, DELETE_NOT_IMPLEMENTED)
    }
}