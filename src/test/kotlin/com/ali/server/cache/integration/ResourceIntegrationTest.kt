package com.ali.server.cache.integration

import com.ali.server.cache.model.ResourceResponse
import java.net.URI
import kotlin.test.Test

class ResourceIntegrationTest : AbstractIntegrationTest() {


    @Test
    fun `when an existing resource requested, then return the resource` () {
         val resource = restTemplate.getForEntity(URI.create("/nameSpace1/id1"), ResourceResponse::class.java)
    }
}