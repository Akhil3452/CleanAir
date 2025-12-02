package uk.ac.tees.mad.cleanair.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "symptoms")
data class SymptomEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String = Date().toString(),
    val symptoms: String,
    val comfortLevel: Int,
    val notes: String? = null
)
