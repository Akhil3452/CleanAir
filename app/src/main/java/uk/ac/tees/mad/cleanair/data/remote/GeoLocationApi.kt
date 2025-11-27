package uk.ac.tees.mad.cleanair.data.remote

import retrofit2.http.GET
import uk.ac.tees.mad.cleanair.data.remote.model.GeoData


interface GeoLocationApi {
    @GET("json/")
    suspend fun getDetails(): GeoData
}