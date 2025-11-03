package com.ali.server.cache.helper

import com.ali.server.cache.model.Resource
import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.util.Base64

@Component
class ETagCalculator {
    companion object {
        private const val SHA_256 = "SHA-256"
    }

    private val encoder = Base64.getUrlEncoder().withoutPadding();

    fun eTagOf(resource: Resource): String {
        val eTagFormat = "${resource.nameSpace}:${resource.id}:${resource.version}:${resource.updatedAt.toEpochMilli()}"
        val hash = MessageDigest.getInstance(SHA_256).digest(eTagFormat.toByteArray())
        return "\"${encoder.encodeToString(hash)}\""
    }
}