package edu.nd.pmcburne.hello

import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import retrofit2.http.GET
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType

interface PlacemarkApiService {
    @GET("~wxt4gm/placemarks.json")
    suspend fun getPlacemarks(): List<PlacemarkDto>
}

object PlacemarkApi {
    private const val BASE_URL = "https://www.cs.virginia.edu/"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    val retrofitService: PlacemarkApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(PlacemarkApiService::class.java)
    }
}