package uk.ac.tees.mad.cleanair.ui.screens.symptoms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import uk.ac.tees.mad.cleanair.data.local.SymptomEntity
import uk.ac.tees.mad.cleanair.data.repository.SymptomsRepository
import javax.inject.Inject

@HiltViewModel
class SymptomsViewModel @Inject constructor(
    private val repository: SymptomsRepository
) : ViewModel() {

    val symptoms = repository.getSymptoms()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addSymptom(symptom: SymptomEntity) {
        viewModelScope.launch {
            repository.addSymptom(symptom)
        }
    }

    fun deleteSymptom(symptom: SymptomEntity) {
        viewModelScope.launch {
            repository.deleteSymptom(symptom)
        }
    }
}
