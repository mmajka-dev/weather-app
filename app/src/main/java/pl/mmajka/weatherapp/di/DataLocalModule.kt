package pl.mmajka.weatherapp.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import pl.mmajka.weatherapp.data.local.WeatherDatabase

private const val DATABASE_NAME = "weather_db"

val dataLocalModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            WeatherDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
    single { get<WeatherDatabase>().cityDao() }
}