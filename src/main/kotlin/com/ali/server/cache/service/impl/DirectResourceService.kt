package com.ali.server.cache.service.impl

import com.ali.server.cache.configuration.annotation.StrategyQualified
import com.ali.server.cache.event.DeleteResourceEvent
import com.ali.server.cache.event.UpdateResourceEvent
import com.ali.server.cache.exception.ResourceNotFoundException
import com.ali.server.cache.model.Resource
import com.ali.server.cache.model.ResourceRequest
import com.ali.server.cache.model.ResourceResponse
import com.ali.server.cache.model.toResourceResponse
import com.ali.server.cache.model.updateFromResourceRequest
import com.ali.server.cache.repository.ResourceRepository
import com.ali.server.cache.service.ResourceService
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