package com.ali.server.cache.service.impl

import com.ali.server.cache.enums.ResourceStrategy
import com.ali.server.cache.enums.StrategyMap
import com.ali.server.cache.helper.RequestPreference
import com.ali.server.cache.model.ResourceRequest
import com.ali.server.cache.service.ResourceService
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

@Service
@Primary
class DelegatingResourceService(val services: StrategyMap, val requestPreference: RequestPreference) : ResourceService {

    private fun pick(): ResourceService {
        val strategy = if (requestPreference.isBypassRequested) ResourceStrategy.DIRECT else ResourceStrategy.CACHED
        return services[strategy]!!
    }

    override fun getResource(nameSpace: String, id: String) = pick().getResource(nameSpace, id)

    override fun getManyResourcesInNameSpace(
        nameSpace: String,
        ids: List<String>
    ) = pick().getManyResourcesInNameSpace(nameSpace, ids)

    override fun putResource(id: String, nameSpace: String, resourceRequest: ResourceRequest) = pick().putResource(
        id, nameSpace, resourceRequest
    )

    override fun deleteResource(nameSpace: String, id: String) = pick().deleteResource(nameSpace, id)

}