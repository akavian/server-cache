package com.ali.server.`c@che`.service

import com.ali.server.`c@che`.model.ResourceRequest
import com.ali.server.`c@che`.model.ResourceResponse

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