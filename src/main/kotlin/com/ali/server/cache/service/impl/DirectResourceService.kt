package com.ali.server.cache.service.impl

import com.ali.server.cache.model.Resource
import com.ali.server.cache.service.ResourceService
import org.springframework.stereotype.Service

@Service(ResourceService.DIRECT_SERVICE)
class DirectResourceService : ResourceService {
    override fun getResource(nameSpace: String, id: String): Resource {
        TODO("Not yet implemented")
    }


    override fun getManyResourcesInNameSpace(ids: List<String>): List<Resource> {
        TODO("Not yet implemented")
    }

    override fun putResource(
        nameSpace: String,
        id: String,
        resource: Resource
    ) {
        TODO("Not yet implemented")
    }

    override fun deleteResource(nameSpace: String, id: String) {
        TODO("Not yet implemented")
    }
}