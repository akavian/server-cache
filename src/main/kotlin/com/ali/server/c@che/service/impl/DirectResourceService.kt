package com.ali.server.`c@che`.service.impl

import com.ali.server.`c@che`.configuration.annotation.StrategyQualified
import com.ali.server.`c@che`.event.DeleteResourceEvent
import com.ali.server.`c@che`.event.UpdateResourceEvent
import com.ali.server.`c@che`.exception.ResourceNotFoundException
import com.ali.server.`c@che`.model.Resource
import com.ali.server.`c@che`.model.ResourceRequest
import com.ali.server.`c@che`.model.ResourceResponse
import com.ali.server.`c@che`.model.toResourceResponse
import com.ali.server.`c@che`.model.updateFromResourceRequest
import com.ali.server.`c@che`.repository.ResourceRepository
import com.ali.server.`c@che`.service.ResourceService
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