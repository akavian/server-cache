package com.ali.server.cache.configuration.properties

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties(prefix = "mongo")
data class MongoProperties(
    @field:NotBlank
    val host: String,
    @field:NotBlank
    val appUser: String,
    val appPass: CharArray,
    @field:Min(1)
    val port: Int,
    @field:NotBlank
    val authDB: String,
    @field:Min(1)
    val maxPoolSize: Int,
    @field:Min(1)
    val minPoolSize: Int,
    @field:Min(1)
    val initialCapacity: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MongoProperties

        if (port != other.port) return false
        if (maxPoolSize != other.maxPoolSize) return false
        if (minPoolSize != other.minPoolSize) return false
        if (initialCapacity != other.initialCapacity) return false
        if (host != other.host) return false
        if (appUser != other.appUser) return false
        if (!appPass.contentEquals(other.appPass)) return false
        if (authDB != other.authDB) return false

        return true
    }

    override fun hashCode(): Int {
        var result = port
        result = 31 * result + maxPoolSize
        result = 31 * result + minPoolSize
        result = 31 * result + initialCapacity
        result = 31 * result + host.hashCode()
        result = 31 * result + appUser.hashCode()
        result = 31 * result + appPass.contentHashCode()
        result = 31 * result + authDB.hashCode()
        return result
    }
}