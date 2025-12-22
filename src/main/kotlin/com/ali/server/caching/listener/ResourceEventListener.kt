package com.ali.server.caching.listener

import com.ali.server.caching.event.ResourceEvent
import com.ali.server.caching.model.Resource
import com.github.benmanes.caffeine.cache.LoadingCache
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ResourceEventListener(private val resourceCache: LoadingCache<String, Resource>) {

    @EventListener
    fun applyResourceEvent(event: ResourceEvent) = resourceCache.invalidate(event.id)
}
