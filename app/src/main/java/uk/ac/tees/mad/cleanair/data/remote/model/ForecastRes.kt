package uk.ac.tees.mad.cleanair.data.remote.model

data class ForecastRes(
    val elevation: Int,
    val generationtime_ms: Double,
    val hourly: HourlyX,
    val hourly_units: HourlyUnitsX,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezone_abbreviation: String,
    val utc_offset_seconds: Int
)