package uk.ac.tees.mad.cleanair.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.ac.tees.mad.cleanair.data.remote.AqiApi

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    val BASE_URL = "https://air-quality-api.open-meteo.com/"
    @Provides
    fun providesRetrofit(): Retrofit{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    fun providesGeoLocationApi(retrofit: Retrofit): AqiApi {
        return retrofit.create(AqiApi::class.java)
    }
}