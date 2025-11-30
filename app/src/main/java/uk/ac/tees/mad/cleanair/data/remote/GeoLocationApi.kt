package uk.ac.tees.mad.cleanair.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import uk.ac.tees.mad.cleanair.data.remote.model.AqiRes
import uk.ac.tees.mad.cleanair.data.remote.model.ForecastRes


interface AqiApi {
    @GET("v1/air-quality")
    suspend fun getDetails(
        @Query("latitude") latitude : Double,
        @Query("longitude") longitude : Double,
        @Query("hourly") hourly: String = "us_aqi",
    ): AqiRes
}

interface ForecastApi{
    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude") latitude : Double,
        @Query("longitude") longitude : Double,
        @Query("hourly") hourly: String = "temperature_2m,relative_humidity_2m,wind_speed_10m",
        @Query("timezone") timezone: String = "auto",
        @Query("forecast_days") forecast_days: Int = 1
    ) : ForecastRes
}