package pl.mmajka.weatherapp

import android.app.Application
import coil.Coil
import coil.ImageLoader
import okhttp3.OkHttpClient
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import pl.mmajka.weatherapp.di.dataLocalModule
import pl.mmajka.weatherapp.di.networkModule
import pl.mmajka.weatherapp.di.repositoryModule
import pl.mmajka.weatherapp.di.useCaseModule
import pl.mmajka.weatherapp.di.viewModelModule

class WeatherApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WeatherApp)
            modules(
                dataLocalModule,
                networkModule,
                repositoryModule,
                useCaseModule,
                viewModelModule
            )
        }
        Coil.setImageLoader(
            ImageLoader.Builder(this)
                .okHttpClient(get<OkHttpClient>())
                .crossfade(true)
                .build()
        )
    }
}
