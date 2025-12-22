package com.ali.server.caching.service.impl

import com.ali.server.caching.configuration.annotation.StrategyQualified
import com.ali.server.caching.event.DeleteResourceEvent
import com.ali.server.caching.event.UpdateResourceEvent
import com.ali.server.caching.exception.ResourceNotFoundException
import com.ali.server.caching.model.Resource
import com.ali.server.caching.model.ResourceRequest
import com.ali.server.caching.model.ResourceResponse
import com.ali.server.caching.model.toResourceResponse
import com.ali.server.caching.model.updateFromResourceRequest
import com.ali.server.caching.repository.ResourceRepository
import com.ali.server.caching.service.ResourceService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service(ResourceService.DIRECT_SERVICE)
@StrategyQualified
class DirectResourceService(
    private val resourceRepository: ResourceRepository,
    private val publisher: ApplicationEventPublisher
) : ResourceService {

    override fun getResource(nameSpace: String, id: String): ResourceResponse {
        return resourceRepository.findById(Resource.getKey(nameSpace, id))
            .orElseThrow { ResourceNotFoundException(Resource.getKey(nameSpace, id)) }
            .toResourceResponse()
    }

    override fun getManyResourcesInNameSpace(nameSpace: String, ids: List<String>): List<ResourceResponse> {
        return resourceRepository.findAllById(ids.map { Resource.getKey(nameSpace, it) })
            .also { if (it.isEmpty()) throw ResourceNotFoundException(ids.joinToString()) }
            .map { it.toResourceResponse() }
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
        publisher.publishEvent(UpdateResourceEvent(key))
    }

    override fun deleteResource(nameSpace: String, id: String) {
        val key = Resource.getKey(nameSpace, id)
        resourceRepository.deleteById(key)
        publisher.publishEvent(DeleteResourceEvent(key))
    }

}