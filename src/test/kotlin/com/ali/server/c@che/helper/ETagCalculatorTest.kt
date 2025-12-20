package com.ali.server.`c@che`.helper

import com.ali.server.`c@che`.model.ResourceResponse
import java.time.Instant
import kotlin.test.Test

class ETagCalculatorTest {

    private val eTagCalculator = ETagCalculator()
    private val resourceResponse =
        ResourceResponse(
            "id",
            "ns",
            mapOf("hello" to "world"),
            1,
            Instant.ofEpochMilli(1000)
        )

    @Test
    fun `when eTag of resource response requested then calculate the eTag`() {
        val eTag = eTagCalculator.eTagOf(resourceResponse)
        assert(eTag.matches(Regex("\"[^\"]+\"")))
    }


}