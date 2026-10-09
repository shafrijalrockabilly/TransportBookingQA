package com.industri.transportqa

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface FlightApiService {
    @GET("api/v1/flights/availabilities")
    suspend fun checkAvailability(): List<String>
}

class MockWebServerTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: FlightApiService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FlightApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `checkAvailability harus mengembalikan daftar penerbangan saat response 200 OK`() = runBlocking {
        val mockJson = """["GA-102 (CGK - SUB)", "GA-204 (CGK - DPS)"]"""
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(mockJson)

        mockWebServer.enqueue(mockResponse)

        val result = apiService.checkAvailability()

        assertThat(result).hasSize(2)
        assertThat(result[0]).contains("CGK - SUB")
    }
}
