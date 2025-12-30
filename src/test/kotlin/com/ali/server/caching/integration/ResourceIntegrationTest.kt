package com.ali.server.caching.integration


import com.ali.server.caching.helper.ETagCalculator
import com.ali.server.caching.model.ResourceResponse
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.testcontainers.shaded.com.google.common.net.HttpHeaders
import kotlin.test.Test

internal class ResourceIntegrationTest : AbstractIntegrationTest() {

    @LocalServerPort
    private var port: Int = 0
    private var domain = "localhost"

    @Autowired
    private lateinit var eTagCalculator: ETagCalculator

    @Test
    fun `when an existing resource requested, then return the resource with cache`() {
        val docId = "id1"
        val nameSpace = "nameSpace1"

        client.get().uri(
            "http://{domain}:{port}/api/resource/{nameSpace}/{docId}", domain, port, nameSpace, docId
        ).exchange().expectStatus().isOk.expectBody(ResourceResponse::class.java).consumeWith {
            val body = it.responseBody ?: error("Response body is null")
            assertThat(body.docId).isEqualTo(docId)
            assertThat(body.nameSpace).isEqualTo(nameSpace)
            assertThat(body.content).size().isEqualTo(1)

        }
    }

    @Test
    fun `when an existing resource requested with the same existing eTag, return not modified with cache`() {
        val docId = "id1"
        val nameSpace = "nameSpace1"

        val responseBody = client.get().uri(
            "http://{domain}:{port}/api/resource/{nameSpace}/{docId}",
            domain, port, nameSpace, docId
        ).exchange().expectBody(ResourceResponse::class.java).returnResult().responseBody
            ?: error("Response body is null")

        val eTag = eTagCalculator.eTagOf(responseBody)
        client.get().uri("http://{domain}:{port}/api/resource/{nameSpace}/{docId}", domain, port, nameSpace, docId)
            .header(HttpHeaders.IF_NONE_MATCH, eTag)
            .exchange().expectStatus().isNotModified
    }

    @Test
    fun `when multiple existing resources requested, then return the resources with cache`() {
        val docIds = listOf("id1", "id2")
        val nameSpace = "nameSpace1"

        client.get()
            .uri {
                it.host(domain)
                    .port(port)
                    .path("api/resource/")
                    .pathSegment("{nameSpace}")
                    .queryParam(
                        "ids",
                        docIds.joinToString(",")
                    )
                    .build(nameSpace)
            }
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(object : ParameterizedTypeReference<List<ResourceResponse>>() {})
            .consumeWith {
                val bodyList = it.responseBody
                assertThat(bodyList).hasSize(2)
            }
    }

    @Test
    fun `when multiple non-existing resources requested, then return empty list with cache`() {
        val docIds = listOf("id5", "id6")
        val nameSpace = "nameSpace1"

        client.get().uri {
            it.host(domain)
                .port(port)
                .path("api/resource/")
                .pathSegment("{nameSpace}")
                .queryParam("ids", docIds.joinToString(","))
                .build(nameSpace)
        }.exchange()
            .expectStatus()
            .isOk
            .expectBody(object : ParameterizedTypeReference<List<ResourceResponse>>() {})
            .consumeWith {
                val body = it.responseBody
                assertThat(body).isEmpty()
            }
    }

    @Test
    fun `when multiple existing and non-existing resources requested, then return a list of existing ones with cache`() {
        val docIds = listOf("id1", "id6")
        val nameSpace = "nameSpace1"

        client.get().uri {
            it.host(domain)
                .port(port)
                .path("api/resource/")
                .pathSegment("{nameSpace}")
                .queryParam("ids", docIds.joinToString(","))
                .build(nameSpace)
        }.exchange()
            .expectStatus()
            .isOk
            .expectBody(object : ParameterizedTypeReference<List<ResourceResponse>>() {})
            .consumeWith {
                val body = it.responseBody
                assertThat(body).isNotEmpty()
                assertThat(body).hasSize(1)
            }
    }
}
