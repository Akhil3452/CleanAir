package uk.ac.tees.mad.cleanair.ui.screens.healthtips

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HealthTipsViewModel @Inject constructor() : ViewModel() {

    private val _tips = MutableStateFlow<List<HealthTip>>(emptyList())
    val tips: StateFlow<List<HealthTip>> = _tips

    fun loadTipsForAqi(aqi: Int) {
        viewModelScope.launch {
            _tips.value = when {
                aqi <= 50 -> listOf(
                    HealthTip("Enjoy the Outdoors", "The air quality is great! Go for a walk or exercise outside."),
                    HealthTip("Maintain Indoor Plants", "They help keep your indoor air fresh and natural.")
                )
                aqi <= 100 -> listOf(
                    HealthTip("Limit Outdoor Activity", "Mild air pollution. Consider shorter outdoor exercises."),
                    HealthTip("Keep Windows Closed", "Avoid letting in moderate pollutants during traffic hours.")
                )
                else -> listOf(
                    HealthTip("Use Air Purifier", "Air quality is unhealthy. Stay indoors with filtered air."),
                    HealthTip("Wear a Mask", "Use N95 masks when stepping outdoors."),
                    HealthTip("Hydrate Well", "Drink more water to ease breathing under poor air quality.")
                )
            }
        }
    }

    fun toggleBookmark(tip: HealthTip) {
        _tips.value = _tips.value.map {
            if (it.title == tip.title) it.copy(bookmarked = !it.bookmarked) else it
        }
    }

    fun shareTip(context: Context, tip: HealthTip) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "${tip.title}\n\n${tip.description}")
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(sendIntent, "Share Tip via"))
    }
}

data class HealthTip(
    val title: String,
    val description: String,
    val bookmarked: Boolean = false
)
