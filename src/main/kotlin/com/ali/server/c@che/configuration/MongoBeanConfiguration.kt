package com.ali.server.`c@che`.configuration

import com.ali.server.`c@che`.configuration.properties.MongoProperties
import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory

@Configuration
@EnableMongoAuditing
class MongoBeanConfiguration(val mongoProperties: MongoProperties) {

    @Bean
    fun mongoBean(): MongoClient {
        val credential = MongoCredential.createCredential(
            mongoProperties.username,
            mongoProperties.authDB,
            mongoProperties.password.toCharArray()
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

    @Bean
    fun mongoDatabaseFactory(client: MongoClient): MongoDatabaseFactory {
        return SimpleMongoClientDatabaseFactory(client, mongoProperties.database)
    }
}