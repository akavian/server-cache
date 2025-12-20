package com.ali.server.`c@che`.service.impl

import com.ali.server.`c@che`.enums.ResourceStrategy
import com.ali.server.`c@che`.helper.RequestPreference
import com.ali.server.`c@che`.model.ResourceRequest
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class DelegatingResourceServiceTest {


    private val cachedStrategyService = mock<CachedResourceService>()
    private val directStrategyService = mock<DirectResourceService>()

    @Spy
    private var strategyMap = mapOf(
        ResourceStrategy.CACHED to cachedStrategyService,
        ResourceStrategy.DIRECT to directStrategyService
    )

    @Mock
    private lateinit var requestPreference: RequestPreference

    @InjectMocks
    private lateinit var delegatingResourceService: DelegatingResourceService

    private val resourceRequest = ResourceRequest(mapOf("hello" to "world"), 0)

    @Test
    fun `when getResource requested with direct strategy, then verify direct strategy is picked`() {

        whenever(requestPreference.isBypassRequested).thenReturn(true)
        delegatingResourceService.getResource("ns", "id")
        verify(directStrategyService).getResource(any(),any())
    }

    @Test
    fun `when getResource requested with cached strategy, then verify cached strategy is picked`() {
        whenever(requestPreference.isBypassRequested).thenReturn(false)
        delegatingResourceService.getResource("ns", "id")
        verify(cachedStrategyService).getResource(any(),any())
    }

    @Test
    fun `when getManyResourcesInNameSpace requested with direct strategy, then verify direct strategy is picked`() {
        whenever(requestPreference.isBypassRequested).thenReturn(true)
        delegatingResourceService.getManyResourcesInNameSpace("ns", listOf("id"))
        verify(directStrategyService).getManyResourcesInNameSpace(any(),any())
    }

    @Test
    fun `when getManyResourcesInNameSpace requested with cached strategy, then verify cached strategy is picked`() {
        whenever(requestPreference.isBypassRequested).thenReturn(false)
        delegatingResourceService.getManyResourcesInNameSpace("ns", listOf("id"))
        verify(cachedStrategyService).getManyResourcesInNameSpace(any(),any())
    }

    @Test
    fun `when putResource requested with direct strategy, then verify direct strategy is picked`() {
        whenever(requestPreference.isBypassRequested).thenReturn(true)
        delegatingResourceService.putResource("ns", "id", resourceRequest )
        verify(directStrategyService).putResource(any(),any(), any())
    }

    @Test
    fun `when putResource requested with cached strategy, then verify cached strategy is picked`() {
        whenever(requestPreference.isBypassRequested).thenReturn(false)
        delegatingResourceService.putResource("ns", "ns", resourceRequest)
        verify(cachedStrategyService).putResource(any(),any(), any())
    }

    @Test
    fun `when deleteResource requested with direct strategy, then verify direct strategy is picked`() {
        whenever(requestPreference.isBypassRequested).thenReturn(true)
        delegatingResourceService.deleteResource("ns", "id")
        verify(directStrategyService).deleteResource(any(),any())
    }

    @Test
    fun `when deleteResource requested with cached strategy, then verify cached strategy is picked`() {
        whenever(requestPreference.isBypassRequested).thenReturn(false)
        delegatingResourceService.deleteResource("ns", "id")
        verify(cachedStrategyService).deleteResource(any(),any())
    }

}