package uk.ac.tees.mad.cleanair.ui.screens.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.cleanair.data.remote.AqiApi
import uk.ac.tees.mad.cleanair.data.remote.ForecastApi
import uk.ac.tees.mad.cleanair.data.remote.model.ForecastRes

@HiltViewModel
class DashboardViewModel
@Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val api: AqiApi,
    private val forecastApi : ForecastApi
) : ViewModel() {

    val aqi = MutableStateFlow<Int?>(null)
    private val _forecast = MutableStateFlow<ForecastRes?>(null)
    val forecast : StateFlow<ForecastRes?> = _forecast

    fun fetchAqi(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                val geoData = api.getDetails(latitude, longitude)
                aqi.value = geoData.hourly.us_aqi.first()
            } catch (e: Exception) {
                Log.e("GeoLocation", "Error fetching data: ${e.message}")
            }
        }
    }

    fun fetchForecast(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                val forecastData = forecastApi.getForecast(latitude, longitude)
                _forecast.value = forecastData
                Log.d("Forecast", "Forecast data: $forecastData")
            } catch (e: Exception) {
                Log.e("Forecast", "Error fetching data: ${e.message}")
            }
        }
    }
}