package com.ali.server.caching.integration

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.client.RestTestClient
import org.testcontainers.containers.BindMode
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.mongodb.MongoDBContainer

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
@Testcontainers
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal abstract class AbstractIntegrationTest {

    companion object {
        private const val HOST_PATH = "mongo-init"
        private const val CONTAINER_PATH = "/docker-entrypoint-initdb.d"

        @JvmStatic
        @Container
        @ServiceConnection
        val mongoDBContainer = MongoDBContainer("mongo:8.2.2")
            .withClasspathResourceMapping(
                HOST_PATH,
                CONTAINER_PATH,
                BindMode.READ_ONLY
            )
    }

    @Autowired
    protected lateinit var client: RestTestClient

    @BeforeAll
    fun seedMongo() {
        mongoDBContainer.execInContainer(
            "bash", "-lc",
            """
                mongoimport \
                --db test \
                --collection resources \
                --file /docker-entrypoint-initdb.d/data.json \
                --jsonArray
    """.trimIndent()
        )
    }

}