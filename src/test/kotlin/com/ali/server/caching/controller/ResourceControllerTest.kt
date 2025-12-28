package com.ali.server.caching.controller

import com.ali.server.caching.helper.ETagCalculator
import com.ali.server.caching.model.Resource
import com.ali.server.caching.model.toResourceResponse
import com.ali.server.caching.service.ResourceService
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean
import org.springframework.test.web.servlet.client.RestTestClient
import org.testcontainers.shaded.com.github.dockerjava.core.MediaType
import java.time.Instant
import kotlin.test.Test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureRestTestClient
internal class ResourceControllerTest() {

    @MockitoBean
    lateinit var resourceService: ResourceService

    @MockitoSpyBean
    lateinit var eTagCalculator: ETagCalculator

    private val resource = Resource.create(
        "xyz", "test", mapOf(
            "subject" to "hello",
            "task" to "learn kotlin",
        ), Instant.now(),
        Instant.now()
    )

    @Autowired
    lateinit var client: RestTestClient

    private val mapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    @Test
    fun `when requested resource exist with no eTag then return the resource`() {
        whenever(resourceService.getResource(any(), any())).thenReturn(resource.toResourceResponse())

        client

        client.get().uri("/api/resource/test/xyz").exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo("xyz")
            .jsonPath("$.nameSpace").isEqualTo("test")
            .jsonPath("$.content.subject").isEqualTo("hello")
            .jsonPath("$.content.task").isEqualTo("learn kotlin")

    }

    @Test
    fun `when requested resource exist with matching eTag then return nothing`() {
        whenever(resourceService.getResource(any(), any()))
            .thenReturn(resource.toResourceResponse())

        val eTag = eTagCalculator.eTagOf(resource.toResourceResponse())

        client.get().uri("/api/resource/test/xyz")
            .header(HttpHeaders.IF_NONE_MATCH, eTag).exchange()
            .expectStatus().isNotModified
            .expectBody().isEmpty
    }

    @Test
    fun `when resource must be created then create and return nothing`() {

        client.put().uri("/api/resource/test/new")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.mediaType)
            .body(mapper.writeValueAsString(resource)).exchange()
            .expectStatus().isAccepted
    }

    @Test
    fun `when resource request is null then error must be thrown`() {

        client.put().uri("/api/resource/test/new")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.mediaType)
            .exchange()
            .expectStatus()
            .isBadRequest
    }

    @Test
    fun `when namespace resources requested then return a list of the requested ones`() {

        whenever(
            resourceService.getManyResourcesInNameSpace(any(), any())
        ).thenReturn(listOf(resource.toResourceResponse()))

        client.get().uri("/api/resource/test?ids=xyz").exchange().expectStatus().isOk
            .expectBody().jsonPath("$").isArray
    }

    @Test
    fun `when namespace resources requested but none exist then return an empty array`() {
        whenever(resourceService.getManyResourcesInNameSpace(any(), any()))
            .thenReturn(emptyList())

        client.get().uri("/api/resource/test?ids=xyz").exchange().expectStatus().isOk
            .expectBody().jsonPath("$").isArray
    }

    @Test
    fun `when deletion of resource requested then return no content`() {
        client.delete().uri("/api/resource/test/xyz").exchange().expectStatus().isNoContent

    }
}

