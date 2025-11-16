package com.ali.server.cache.configuration

import com.ali.server.cache.configuration.properties.ResourceCacheProperties
import com.ali.server.cache.exception.ResourceNotFoundException
import com.ali.server.cache.model.Resource
import com.ali.server.cache.repository.ResourceRepository
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
@ConfigurationPropertiesScan
class ResourceCacheBeanConfiguration(val resourceCacheProperties: ResourceCacheProperties) {

    @Bean
    fun resourceCache(resourceRepository: ResourceRepository): LoadingCache<String, Resource> =
        Caffeine.newBuilder()
            .expireAfterWrite(resourceCacheProperties.expireAfterWrite, TimeUnit.SECONDS)
            .initialCapacity(resourceCacheProperties.initialCapacity)
            .build {
                resourceRepository.findById(it).orElseThrow { ResourceNotFoundException(it) }
            }
}