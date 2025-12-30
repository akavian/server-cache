package com.ali.server.caching.configuration

import com.ali.server.caching.exception.ResourceNotFoundException
import com.ali.server.caching.model.Resource
import com.ali.server.caching.repository.ResourceRepository
import com.github.benmanes.caffeine.cache.CacheLoader

class CaffeineLoader(private val repository: ResourceRepository) : CacheLoader<String, Resource?> {
    override fun load(key: String): Resource {
        return repository.findById(key).orElseThrow { ResourceNotFoundException(key) }
    }

    override fun loadAll(keys: Set<String>): Map<String, Resource> {
        return repository.findAllById(keys).associateBy { it.key }
    }
}