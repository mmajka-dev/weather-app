package pl.mmajka.weatherapp.di

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import pl.mmajka.weatherapp.BuildConfig
import pl.mmajka.weatherapp.data.remote.api.WeatherApi
import pl.mmajka.weatherapp.data.remote.interceptors.ApiKeyInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val networkModule = module {
    single { ApiKeyInterceptor(BuildConfig.API_KEY) }

    single {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<ApiKeyInterceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single {
        val json = Json { ignoreUnknownKeys = true }
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(get())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    single { get<Retrofit>().create(WeatherApi::class.java) }
}
