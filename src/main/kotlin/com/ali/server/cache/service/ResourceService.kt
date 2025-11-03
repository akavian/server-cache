package com.ali.server.cache.service

import com.ali.server.cache.model.Resource

interface ResourceService {

    companion object {
        const val CACHED_SERVICE = "cached"
        const val DIRECT_SERVICE = "direct"
    }

    fun getResource(nameSpace: String, id: String): Resource
    fun getManyResourcesInNameSpace(ids: List<String>): List<Resource>
    fun putResource(nameSpace: String, id: String, resource: Resource)
    fun deleteResource(nameSpace: String, id: String)
}