package com.ali.server.caching.service.impl

import com.ali.server.caching.enums.ResourceStrategy
import com.ali.server.caching.enums.StrategyMap
import com.ali.server.caching.helper.RequestPreference
import com.ali.server.caching.model.ResourceQueryExample
import com.ali.server.caching.model.ResourceRequest
import com.ali.server.caching.service.ResourceService
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

@Service
@Primary
class DelegatingResourceService(
    private val services: StrategyMap,
    private val requestPreference: RequestPreference
) : ResourceService {

    private fun pick(isExample: Boolean = false): ResourceService {
        val strategy = when {
            isExample -> ResourceStrategy.DIRECT
            requestPreference.isBypassRequested -> ResourceStrategy.DIRECT
            else -> ResourceStrategy.CACHED
        }
        return services[strategy]!!
    }

    override fun getResource(nameSpace: String, id: String) = pick().getResource(nameSpace, id)

    override fun getManyResourcesInNameSpace(
        nameSpace: String,
        ids: List<String>
    ) = pick().getManyResourcesInNameSpace(nameSpace, ids)

    override fun putResource(id: String, nameSpace: String, resourceRequest: ResourceRequest) =
        pick().putResource(id, nameSpace, resourceRequest)

    override fun deleteResource(nameSpace: String, id: String) =
        pick().deleteResource(nameSpace, id)

    override fun getExamples(resourceQueryExample: ResourceQueryExample) =
        pick(true).getExamples(resourceQueryExample)

}