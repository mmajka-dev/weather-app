package pl.mmajka.weatherapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.mmajka.weatherapp.data.local.dao.CityDao
import pl.mmajka.weatherapp.data.local.model.CityEntity

@Database(entities = [CityEntity::class], version = 2, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
}
