package uk.ac.tees.mad.cleanair.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SymptomEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun symptomDao(): SymptomDao
}
