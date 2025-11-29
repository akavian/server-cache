package com.ali.server.cache.listener

import com.ali.server.cache.event.ResourceEvent
import com.ali.server.cache.model.Resource
import com.github.benmanes.caffeine.cache.LoadingCache
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ResourceEventListener(private val resourceCache: LoadingCache<String, Resource>) {

    @EventListener
    fun applyResourceEvent(event: ResourceEvent) = resourceCache.invalidate(event.id)
}
