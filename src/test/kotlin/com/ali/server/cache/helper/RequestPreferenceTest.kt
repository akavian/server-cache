package com.ali.server.cache.helper

import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class RequestPreferenceTest {

    @Mock
    private lateinit var httpRequest: HttpServletRequest

    @Test
    fun `when CACHE_CONTROLL is NO_CACHE then isBypassRequested should be true`() {
        whenever(httpRequest.getHeader(any())).thenReturn("no-cache")
        val requestPreference = RequestPreference(httpRequest)
        assert(requestPreference.isBypassRequested)
    }

    @Test
    fun `when CACHE_CONTROLL is not NO_CACHE then isBypassRequested should be true`() {
        whenever(httpRequest.getHeader(any())).thenReturn("something")
        val requestPreference = RequestPreference(httpRequest)
        assert(!requestPreference.isBypassRequested)
    }

}