package com.ali.server.cache.integration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
abstract class AbstractIntegrationTest {

    companion object {
        private const val HOST_PATH = "/src/test/resources/mongo-init"
        private const val CONTAINER_PATH = "/docker-entrypoint-initdb.d"

        @JvmStatic
        @Container
        @ServiceConnection
        val mongoDBContainer = MongoDBContainer("mongo:8.2.2").withFileSystemBind(
            HOST_PATH,
            CONTAINER_PATH,
            BindMode.READ_WRITE
        )
    }

    @Autowired
    protected lateinit var template: TestRestTemplate
}