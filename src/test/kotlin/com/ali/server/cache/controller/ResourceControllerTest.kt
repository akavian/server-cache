package com.ali.server.cache.controller

import com.ali.server.cache.helper.ETagCalculator
import com.ali.server.cache.model.Resource
import com.ali.server.cache.model.toResourceResponse
import com.ali.server.cache.service.ResourceService
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put
import java.time.Instant
import kotlin.test.Test

@WebMvcTest(ResourceController::class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Import(ETagCalculator::class)
class ResourceControllerTest(val mockMvc: MockMvc) {

    @MockitoBean
    lateinit var resourceService: ResourceService

    @MockitoSpyBean
    lateinit var eTagCalculator: ETagCalculator

    private val resource = Resource(
        "xyz", "test", mapOf(
            "subject" to "hello",
            "task" to "learn kotlin",
        ), Instant.now()
    )

    private val mapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    @Test
    fun `when requested resource exist with no eTag then return the resource`() {
        whenever(resourceService.getResource(any(), any())).thenReturn(resource.toResourceResponse())

        mockMvc.get("/api/resource/test/xyz").andExpect {
            status { isOk() }
            jsonPath("$.id") { value("xyz") }
            jsonPath("$.nameSpace") { value("test") }
            jsonPath("$.content.subject") { value("hello") }
            jsonPath("$.content.task") { value("learn kotlin") }
        }
    }

    @Test
    fun `when requested resource exist with matching eTag then return nothing`() {
        whenever(resourceService.getResource(any(), any()))
            .thenReturn(resource.toResourceResponse())

        val eTag = eTagCalculator.eTagOf(resource.toResourceResponse())

        mockMvc.get("/api/resource/test/xyz") {
            headers { set("if-none-match", eTag) }
        }.andExpect {
            status { isNotModified() }
            match { it.response.contentLength == 0 }
        }
    }

    @Test
    fun `when resource must be created then create and return nothing`() {

        mockMvc.put("/api/resource/test/new") {
            content = mapper.writeValueAsString(resource)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect { status { isAccepted() } }
    }

    @Test
    fun `when resource request is null then error must be thrown`() {

        mockMvc.put("/api/resource/test/new") {
            content = null
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `when namespace resources requested then return a list of the requested ones`() {

        whenever(
            resourceService.getManyResourcesInNameSpace(any(), any())
        ).thenReturn(listOf(resource.toResourceResponse()))

        mockMvc.get("/api/resource/test?ids=xyz").andExpect {
            status { isOk() }
            jsonPath("$") {
                isArray()
                isNotEmpty()
            }
        }
    }

    @Test
    fun `when namespace resources requested but none exist then return an empty array`() {
        whenever(resourceService.getManyResourcesInNameSpace(any(), any()))
            .thenReturn(emptyList())

        mockMvc.get("/api/resource/test?ids=xyz").andExpect {
            status { isOk() }
            jsonPath("$") {
                isArray()
                isEmpty()
            }
        }
    }

    @Test
    fun `when deletion of resource requested then return no content`() {
        mockMvc.delete("/api/resource/test/xyz").andExpect {
            status { isNoContent() }
        }
    }
}

