package uk.ac.tees.mad.cleanair.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SymptomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(symptom: SymptomEntity)

    @Query("SELECT * FROM symptoms ORDER BY id DESC")
    fun getAllSymptoms(): Flow<List<SymptomEntity>>

    @Delete
    suspend fun delete(symptom: SymptomEntity)
}
