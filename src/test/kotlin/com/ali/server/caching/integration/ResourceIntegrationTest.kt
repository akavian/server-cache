 package com.ali.server.caching.integration


import com.ali.server.caching.helper.ETagCalculator
import com.ali.server.caching.model.ResourceResponse
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.server.LocalServerPort
import org.testcontainers.shaded.com.google.common.net.HttpHeaders
import kotlin.test.Test

internal class ResourceIntegrationTest : AbstractIntegrationTest() {

    @LocalServerPort
    private var port: Int = 0
    private var domain = "localhost"

    @Autowired
    private lateinit var eTagCalculator: ETagCalculator

    @Test
    fun `when an existing resource requested, then return the resource`() {
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
    fun `when an existing resource requested with the same existing etag, return not modified`() {
        val docId = "id1"
        val nameSpace = "nameSpace1"

        val responseBody = client.get().uri(
            "http://{domain}:{port}/api/resource/{nameSpace}/{docId}", domain, port, nameSpace, docId
        ).exchange().expectBody(ResourceResponse::class.java).returnResult().responseBody
            ?: error("Response body is null")

        val eTag = eTagCalculator.eTagOf(responseBody)
        client.get().uri("http://{domain}:{port}/api/resource/{nameSpace}/{docId}", domain, port, nameSpace, docId)
            .header(HttpHeaders.IF_NONE_MATCH, eTag)
            .exchange().expectStatus().isNotModified
    }
}