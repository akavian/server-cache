package com.ali.server.caching.service

import com.ali.server.caching.model.ResourceQueryExample
import com.ali.server.caching.model.ResourceRequest
import com.ali.server.caching.model.ResourceResponse

interface ResourceService {

    companion object {
        const val CACHED_SERVICE = "cached"
        const val DIRECT_SERVICE = "direct"
    }

    fun getResource(nameSpace: String, id: String): ResourceResponse
    fun getManyResourcesInNameSpace(nameSpace: String,ids: List<String>): List<ResourceResponse>
    fun putResource(id: String, nameSpace: String, resourceRequest: ResourceRequest)
    fun deleteResource(nameSpace: String, id: String)
    fun getExamples(resourceQueryExample: ResourceQueryExample): List<ResourceResponse>
}