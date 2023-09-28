package g56055.mobg.meteoapp.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface WeatherService {

    @GET("timeline/{city}?unitGroup=metric&contentType=json")
    suspend fun getWeatherByCity(
        @Path("city") city: String,
        @Query("key") key: String
    ): WeatherResponse

    @GET("timeline/{lat}%2C{long}?unitGroup=metric&contentType=json")
    suspend fun getWeatherByCoordinates(
        @Path("lat") lat: Double,
        @Path("long") long: Double,
        @Query("key") key: String
    ): WeatherResponse
}

object WeatherNetwork {
    private val moshi =
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val weather: WeatherService = retrofit.create(WeatherService::class.java)
}