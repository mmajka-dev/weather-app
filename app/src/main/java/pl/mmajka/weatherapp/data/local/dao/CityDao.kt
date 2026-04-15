package pl.mmajka.weatherapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import pl.mmajka.weatherapp.data.local.model.CityEntity

@Dao
interface CityDao {

    @Query("SELECT * FROM search_history ORDER BY savedAt DESC LIMIT :limit")
    fun getHistory(limit: Int): Flow<List<CityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city: CityEntity)

    @Query(
        "DELETE FROM search_history WHERE id NOT IN " +
            "(SELECT id FROM search_history ORDER BY savedAt DESC LIMIT :limit)"
    )
    suspend fun trimHistory(limit: Int)

    @Transaction
    suspend fun upsertAndTrim(city: CityEntity, limit: Int) {
        insert(city)
        trimHistory(limit)
    }
}
