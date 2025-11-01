package com.ali.server.cache.service.impl

import com.ali.server.cache.model.Resource
import com.ali.server.cache.service.ResourceService
import org.springframework.http.ETag

class CacheResourceService: ResourceService {
    override fun getResource(
        nameSpace: String,
        id: String,
        eTag: ETag?
    ): Resource {
        TODO("Not yet implemented")
    }

    override fun getManyResourcesInNameSpace(ids: List<String>) {
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