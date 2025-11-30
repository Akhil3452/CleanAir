package uk.ac.tees.mad.cleanair.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.ac.tees.mad.cleanair.data.remote.AqiApi
import uk.ac.tees.mad.cleanair.data.remote.ForecastApi

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    val BASE_URL = "https://air-quality-api.open-meteo.com/"
    val FORECAST_URL = "https://api.open-meteo.com/"

    @Provides
    @ForecastRetrofit
    fun providesForecastRetrofit() : Retrofit{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(FORECAST_URL)
            .build()
    }

    @Provides
    @GeoLocationRetrofit
    fun providesRetrofit(): Retrofit{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    fun providesGeoLocationApi(@GeoLocationRetrofit retrofit: Retrofit): AqiApi {
        return retrofit.create(AqiApi::class.java)
    }

    @Provides
    fun providesForecast(@ForecastRetrofit retrofit: Retrofit): ForecastApi {
        return retrofit.create(ForecastApi::class.java)
    }
}