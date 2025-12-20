package com.ali.server.`c@che`.listener

import com.ali.server.`c@che`.event.ResourceEvent
import com.ali.server.`c@che`.model.Resource
import com.github.benmanes.caffeine.cache.LoadingCache
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ResourceEventListener(private val resourceCache: LoadingCache<String, Resource>) {

    @EventListener
    fun applyResourceEvent(event: ResourceEvent) = resourceCache.invalidate(event.id)
}
