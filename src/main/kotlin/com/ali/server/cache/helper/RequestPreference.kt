package com.ali.server.cache.helper

import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST

@Component
@Scope(SCOPE_REQUEST)
class RequestPreference(request: HttpServletRequest) {
    companion object {
        private const val CACHE_CONTROL = "Cache-Control"
        private const val PRAGMA = "Pragma"
        private const val NO_CACHE = "no-cache"
    }

    val isBypassRequested = run {
        val cacheControlHeader = request.getHeader(CACHE_CONTROL) ?: ""
        val pragmaHeader = request.getHeader(PRAGMA) ?: ""
        cacheControlHeader.contains(NO_CACHE, true)
                || pragmaHeader.equals(NO_CACHE, true)
    }
}