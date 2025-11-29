package uk.ac.tees.mad.cleanair.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import uk.ac.tees.mad.cleanair.data.remote.model.AqiRes


interface AqiApi {
    @GET("v1/air-quality")
    suspend fun getDetails(
        @Query("latitude") latitude : Double,
        @Query("longitude") longitude : Double,
        @Query("hourly") hourly: String = "us_aqi"
    ): AqiRes
}