package com.ali.server.cache.service.impl

import com.ali.server.cache.model.Resource
import com.ali.server.cache.model.ResourceRequest
import com.ali.server.cache.model.toResourceResponse
import com.github.benmanes.caffeine.cache.LoadingCache
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.time.Instant
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class CachedResourceServiceTest {

    @Mock
    private lateinit var resourceCache: LoadingCache<String, Resource>

    @InjectMocks
    private lateinit var cachedResourceService: CachedResourceService

    private val resourceRequest = ResourceRequest(mapOf("hello" to "world"), 0)

    private val resource = Resource(
        "id",
        "ns",
        mapOf("hello" to "world"),
        Instant.now(),
        Instant.now(),
        0
    )

    @Test
    fun `when resource get is requested then return the existing resource`() {
        whenever(resourceCache.get(any())).thenReturn(resource)

        val resourceResponse = cachedResourceService.getResource(resource.nameSpace, resource.nameSpace)
        assert(resourceResponse == resource.toResourceResponse())
    }

    @Test
    fun `when resource get as many is requested then return the existing resources`() {
        whenever(resourceCache.getAll(any()))
            .thenReturn(mapOf(Pair(resource.key, resource)))
        val resourceResponses = cachedResourceService.getManyResourcesInNameSpace(resource.nameSpace, listOf(resource.id))

        assert(resourceResponses.size == 1)
        assert(resourceResponses[0] == resource.toResourceResponse() )
    }

    @Test
    fun `when resource put is requested then throw response status exception`() {
        val thrown: ResponseStatusException =
            assertThrows { cachedResourceService.putResource("id", "ns", resourceRequest) }
        assert(thrown.statusCode == HttpStatus.NOT_IMPLEMENTED)
    }

    @Test
    fun `when resource delete is requested then throw response status exception`() {
        val thrown: ResponseStatusException =
            assertThrows { cachedResourceService.deleteResource("ns", "id") }
        assert(thrown.statusCode == HttpStatus.NOT_IMPLEMENTED)
    }

}