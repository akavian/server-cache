package com.ali.server.cache.service.impl

import com.ali.server.cache.exception.ResourceNotFoundException
import com.ali.server.cache.model.Resource
import com.ali.server.cache.repository.ResourceRepository
import com.ali.server.cache.service.ResourceService
import org.springframework.stereotype.Service

@Service(ResourceService.DIRECT_SERVICE)
class DirectResourceService(val resourceRepository: ResourceRepository) : ResourceService {
    override fun getResource(nameSpace: String, id: String): Resource {
        return resourceRepository.findById(Resource.getKey(nameSpace, id))
            .orElseThrow { ResourceNotFoundException(Resource.getKey(nameSpace, id)) }
    }

    override fun getManyResourcesInNameSpace(ids: List<String>): List<Resource> {
        return resourceRepository.findAllById(ids)
    }

    override fun putResource(
        resource: Resource
    ) {
        resourceRepository.save(resource)
    }

    override fun deleteResource(nameSpace: String, id: String) {
        resourceRepository.deleteById(Resource.getKey(nameSpace, id))
    }

}