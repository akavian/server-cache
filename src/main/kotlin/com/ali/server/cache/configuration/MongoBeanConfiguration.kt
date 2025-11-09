package com.ali.server.cache.configuration

import com.ali.server.cache.configuration.properties.MongoProperties
import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MongoBeanConfiguration(val mongoProperties: MongoProperties) {

    @Bean
    fun mongoBean(): MongoClient {
        val credential = MongoCredential.createCredential(
            mongoProperties.appUser,
            mongoProperties.authDB,
            mongoProperties.appPass
        )

        val mongoClientSettings = MongoClientSettings.builder().credential(credential).applyToClusterSettings {
            it.hosts(
                listOf(
                    ServerAddress(mongoProperties.host, mongoProperties.port)
                )
            )
        }.applyToConnectionPoolSettings {
            it.maxSize(mongoProperties.maxPoolSize)
            it.minSize(mongoProperties.minPoolSize)
        }.build()

        return MongoClients.create(mongoClientSettings)
    }
}