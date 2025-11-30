package uk.ac.tees.mad.cleanair.data.remote.model

data class HourlyUnitsX(
    val relative_humidity_2m: String,
    val temperature_2m: String,
    val time: String,
    val wind_speed_10m: String
)