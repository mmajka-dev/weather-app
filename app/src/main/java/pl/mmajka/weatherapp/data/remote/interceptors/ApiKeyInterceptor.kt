package pl.mmajka.weatherapp.data.remote.interceptors

import okhttp3.Interceptor
import okhttp3.Response

private const val APP_ID_PARAMETER = "appid"
class ApiKeyInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .url(
                chain.request().url.newBuilder()
                    .addQueryParameter(APP_ID_PARAMETER, apiKey)
                    .build()
            )
            .build()
        return chain.proceed(request)
    }
}