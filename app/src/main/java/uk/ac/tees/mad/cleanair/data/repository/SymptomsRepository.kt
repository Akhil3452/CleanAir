package uk.ac.tees.mad.cleanair.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.cleanair.data.local.SymptomDao
import uk.ac.tees.mad.cleanair.data.local.SymptomEntity
import javax.inject.Inject

class SymptomsRepository @Inject constructor(
    private val dao: SymptomDao,
    private val firestore: FirebaseFirestore
) {
    fun getSymptoms(): Flow<List<SymptomEntity>> = dao.getAllSymptoms()

    suspend fun addSymptom(symptom: SymptomEntity) {
        dao.insert(symptom)
        firestore.collection("symptoms").add(symptom)
    }

    suspend fun deleteSymptom(symptom: SymptomEntity) {
        dao.delete(symptom)
        firestore.collection("symptoms")
            .whereEqualTo("date", symptom.date)
            .get()
            .addOnSuccessListener { snapshot ->
                snapshot.documents.forEach { it.reference.delete() }
            }
    }
}
