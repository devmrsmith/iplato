package com.iplato.spacex

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.iplato.spacex.api.SpaceXService
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Tests mocking API - MockWebServer used to send responses from local json files (taken from the live API)
 */
@RunWith(JUnit4::class)
class ApiServiceTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: SpaceXService

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpaceXService::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun requestShips() {
        runBlocking {
            enqueueResponse("ships.json")
            val resultResponse = service.getShips("10", "0")

            val request = mockWebServer.takeRequest()
            assertNotNull(resultResponse)
            assertThat(request.path, `is`("/v3/ships?limit=10&offset=0"))
        }
    }

    @Test
    fun getShipsSize() {
        runBlocking {
            enqueueResponse("ships.json")
            val resultResponse = service.getShips("10", "0").body()
            assertThat(resultResponse!!.size, `is`(10))
        }
    }

    @Test
    fun getActiveShipsSize() {
        runBlocking {
            enqueueResponse("ships.json")
            val resultResponse = service.getShips("10", "0").body()
            val shipFilterList = resultResponse?.filter { it.active == true }
            assertThat(shipFilterList?.size, `is`(3))
        }
    }

    @Test
    fun getFirstShipDetail() {
        runBlocking {
            enqueueResponse("ships.json")
            val resultResponse = service.getShips("10", "0").body()
            val ship = resultResponse!![0]
            assertThat(ship.shipId, `is`("AMERICANCHAMPION"))
        }
    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader
            ?.getResourceAsStream("api_responses/$fileName")
        val source = Okio.buffer(Okio.source(inputStream))
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse.setBody(
                source.readString(Charsets.UTF_8)
            )
        )
    }
}
