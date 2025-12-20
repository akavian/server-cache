package com.ali.server.`c@che`.helper

import com.ali.server.`c@che`.model.ResourceResponse
import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.util.Base64

@Component
class ETagCalculator {
    companion object {
        private const val SHA_256 = "SHA-256"
    }

    private val encoder = Base64.getUrlEncoder().withoutPadding();

    fun eTagOf(resourceResponse: ResourceResponse): String {
        val eTagFormat =
            """${resourceResponse.nameSpace}
                |:${resourceResponse.id}
                |:${resourceResponse.version}
                |:${resourceResponse.updatedAt.toEpochMilli()}""".trimMargin()
        val hash = MessageDigest.getInstance(SHA_256).digest(eTagFormat.toByteArray())
        return "\"${encoder.encodeToString(hash)}\""
    }
}