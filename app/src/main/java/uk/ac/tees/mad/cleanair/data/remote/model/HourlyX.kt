package uk.ac.tees.mad.cleanair.data.remote.model

data class HourlyX(
    val relative_humidity_2m: List<Int>,
    val temperature_2m: List<Double>,
    val time: List<String>,
    val wind_speed_10m: List<Double>
)