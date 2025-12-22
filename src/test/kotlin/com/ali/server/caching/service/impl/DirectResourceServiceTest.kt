package com.ali.server.caching.service.impl

import com.ali.server.caching.event.UpdateResourceEvent
import com.ali.server.caching.exception.ResourceNotFoundException
import com.ali.server.caching.model.Resource
import com.ali.server.caching.model.ResourceRequest
import com.ali.server.caching.model.toResourceResponse
import com.ali.server.caching.repository.ResourceRepository
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.context.ApplicationEventPublisher
import java.time.Instant
import java.util.Optional
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class DirectResourceServiceTest {

    @Mock
    lateinit var resourceRepository: ResourceRepository

    @Mock
    lateinit var publisher: ApplicationEventPublisher

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
        verify(publisher, times(0)).publishEvent(any())

    }

    @Test
    fun `when resource is not found should throw not found exception`() {
        whenever(resourceRepository.findById(any())).thenReturn(Optional.empty())
        assertThrows<ResourceNotFoundException> { directResourceService.getResource("ns", "id") }
        verify(publisher, times(0)).publishEvent(any())
    }

    @Test
    fun `when many resources requested and they exist, then return a list of resource payloads`() {
        whenever(resourceRepository.findAllById(any())).thenReturn(listOf(resource))

        val resourcePayloads = directResourceService.getManyResourcesInNameSpace("ns", listOf("id"))
        assert(resourcePayloads.size == 1)
        assert(resourcePayloads.first() == resource.toResourceResponse())
        verify(publisher, times(0)).publishEvent(any())
    }

    @Test
    fun `when update-create requested for the existing resource, then save the resource and publish message`() {
        whenever(resourceRepository.findById(any())).thenReturn(Optional.of(resource))

        directResourceService.putResource(
            "id",
            "ns",
            resource.toResourceRequest()
        )

        verify(resourceRepository, times(1)).findById(any())
        verify(resourceRepository, times(1)).save(any())
        verify(publisher, times(1)).publishEvent(any<UpdateResourceEvent>())
    }

    @Test
    fun `when update-create request for non-existing resource, then save the resource and publish message`() {

        directResourceService.putResource("id", "ns", resource.toResourceRequest())
        verify(resourceRepository, times(1)).findById(any())
        verify(resourceRepository, times(1)).save(any())
        verify(publisher).publishEvent(any<UpdateResourceEvent>())
    }

    private fun Resource.toResourceRequest() = ResourceRequest(this.content, this.version)

}