package com.ali.server.cache.service.impl

import com.ali.server.cache.exception.ResourceNotFoundException
import com.ali.server.cache.model.Resource
import com.ali.server.cache.model.toResourceResponse
import com.ali.server.cache.repository.ResourceRepository
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.time.Instant
import java.util.Optional
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class DirectResourceServiceTest {

    @Mock
    lateinit var resourceRepository: ResourceRepository

    @InjectMocks
    lateinit var directResourceService: DirectResourceService

    private val resource =
        Resource(
            "id",
            "ns",
            mapOf("hello" to "world"),
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
            1L
        )

    @Suppress("IDENTITY_SENSITIVE_OPERATIONS_WITH_VALUE_TYPE")
    @Test
    fun `when resource is requested and it exist then return the resource`() {

        whenever(resourceRepository.findById(any())).thenReturn(Optional.of(resource))

        val resourcePayload = directResourceService.getResource("ns", "id")

        assertNotNull(resourcePayload)
        assert(resourcePayload == resource.toResourceResponse())
    }

    @Test
    fun `when resource is not found should throw not found exception`() {
        whenever(resourceRepository.findById(any())).thenReturn(Optional.empty())
        assertThrows<ResourceNotFoundException> { directResourceService.getResource("ns", "id") }
    }

    @Test
    fun `when many resources requested and they exist, then return a list of resource payloads`() {
        whenever(resourceRepository.findAllById(any())).thenReturn(listOf(resource))

        val resourcePayloads = directResourceService.getManyResourcesInNameSpace("ns", listOf("id"))
        assert(resourcePayloads.size == 1)
        assert(resourcePayloads.first() == resource.toResourceResponse())
    }
}