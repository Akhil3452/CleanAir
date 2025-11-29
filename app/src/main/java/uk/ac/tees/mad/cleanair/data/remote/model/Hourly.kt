package uk.ac.tees.mad.cleanair.data.remote.model

data class Hourly(
    val time: List<String>,
    val us_aqi: List<Int>
)