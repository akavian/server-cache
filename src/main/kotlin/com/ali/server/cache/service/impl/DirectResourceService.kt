package com.ali.server.cache.service.impl

import com.ali.server.cache.exception.ResourceNotFoundException
import com.ali.server.cache.model.Resource
import com.ali.server.cache.model.ResourceRequest
import com.ali.server.cache.model.ResourceResponse
import com.ali.server.cache.model.toResponsePayload
import com.ali.server.cache.model.updateFromResourceRequest
import com.ali.server.cache.repository.ResourceRepository
import com.ali.server.cache.service.ResourceService
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service(ResourceService.DIRECT_SERVICE)
class DirectResourceService(val resourceRepository: ResourceRepository) : ResourceService {
    override fun getResource(nameSpace: String, id: String): ResourceResponse {
        return resourceRepository.findById(Resource.getKey(nameSpace, id))
            .orElseThrow { ResourceNotFoundException(Resource.getKey(nameSpace, id)) }
            .toResponsePayload()
    }

    override fun getManyResourcesInNameSpace(nameSpace: String, ids: List<String>): List<ResourceResponse> {
        return resourceRepository.findAllById(ids.map { Resource.getKey(nameSpace, it) })
            .map { it.toResponsePayload() }
    }

    override fun putResource(
        id: String,
        nameSpace: String,
        resourceRequest: ResourceRequest
    ) {
        val key = Resource.getKey(nameSpace, id)
        val resource =
            resourceRepository.findById(key).getOrNull()?.updateFromResourceRequest(resourceRequest)
                ?: Resource.fromResourceRequest(id, nameSpace, resourceRequest)
        resourceRepository.save(resource)
    }

    override fun deleteResource(nameSpace: String, id: String) {
        resourceRepository.deleteById(Resource.getKey(nameSpace, id))
    }

}