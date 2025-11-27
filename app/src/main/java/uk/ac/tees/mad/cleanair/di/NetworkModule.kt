package uk.ac.tees.mad.cleanair.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.ac.tees.mad.cleanair.data.remote.GeoLocationApi

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    val BASE_URL = "http://ip-api.com/"
    @Provides
    fun providesRetrofit(): Retrofit{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    fun providesGeoLocationApi(retrofit: Retrofit): GeoLocationApi {
        return retrofit.create(GeoLocationApi::class.java)
    }
}