package com.ali.server.cache.service

import com.ali.server.cache.model.Resource
import org.springframework.http.ETag

interface ResourceService {
    fun getResource(nameSpace: String, id: String, eTag: ETag?): Resource
    fun getManyResourcesInNameSpace(ids: List<String>)
    fun putResource(nameSpace: String, id: String, resource: Resource)
    fun deleteResource(nameSpace: String, id: String)
}