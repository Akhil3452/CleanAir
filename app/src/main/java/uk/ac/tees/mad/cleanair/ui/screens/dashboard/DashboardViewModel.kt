package uk.ac.tees.mad.cleanair.ui.screens.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.cleanair.data.remote.AqiApi

@HiltViewModel
class DashboardViewModel
    @Inject constructor(
        private val auth: FirebaseAuth,
        private val firestore: FirebaseFirestore,
        private val api: AqiApi
    )
    : ViewModel() {

        val aqi = MutableStateFlow<Int?>(null)

    fun fetchAqi(latitude: Double, longitude: Double){
        viewModelScope.launch {
            try {
                val geoData = api.getDetails(latitude, longitude)
                aqi.value = geoData.hourly.us_aqi.first()
            } catch (e: Exception) {
                Log.e("GeoLocation", "Error fetching data: ${e.message}")
            }
        }
    }
}