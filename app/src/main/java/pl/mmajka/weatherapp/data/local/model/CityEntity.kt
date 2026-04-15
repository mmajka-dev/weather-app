package pl.mmajka.weatherapp.data.local.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "search_history",
    indices = [Index(value = ["name", "country", "lat", "lon"], unique = true)]
)
data class CityEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val country: String,
    val state: String?,
    val lat: Double,
    val lon: Double,
    val savedAt: Long = System.currentTimeMillis()
)
