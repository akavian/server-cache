package com.ali.server.cache.service

import com.ali.server.cache.model.ResourceRequest
import com.ali.server.cache.model.ResourceResponse

interface ResourceService {

    companion object {
        const val CACHED_SERVICE = "cached"
        const val DIRECT_SERVICE = "direct"
    }

    fun getResource(nameSpace: String, id: String): ResourceResponse
    fun getManyResourcesInNameSpace(nameSpace: String,ids: List<String>): List<ResourceResponse>
    fun putResource(id: String, nameSpace: String, resourceRequest: ResourceRequest)
    fun deleteResource(nameSpace: String, id: String)
}