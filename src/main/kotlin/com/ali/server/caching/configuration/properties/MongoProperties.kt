package com.ali.server.caching.configuration.properties

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Profile
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties(prefix = "mongo")
@Profile("!test")
 class MongoProperties(
    @field:NotBlank
    val host: String,
    @field:Min(1)
    val port: Int,
    @field:NotBlank
    val database: String,
    @field:NotBlank
    val username: String,
    @field:NotEmpty
    val password: String,
    @field:NotBlank
    val authDB: String,
    @field:Min(1)
    val maxPoolSize: Int,
    @field:Min(1)
    val minPoolSize: Int
)