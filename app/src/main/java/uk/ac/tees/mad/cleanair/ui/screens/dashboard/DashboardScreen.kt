package uk.ac.tees.mad.cleanair.ui.screens.dashboard

import android.Manifest
import android.location.LocationProvider
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.cleanair.ui.theme.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DashboardScreen(navController: NavController,
                    viewModel : DashboardViewModel = hiltViewModel()
) {
    val dummyCity = "London"
    val dummyAQI = viewModel.aqi.collectAsState().value
    val dummyTemp = 23
    val dummyHumidity = 54
    val dummyStatus = when (dummyAQI) {
        in 0..50 -> "Good Air Quality 🌤️"
        in 51.. 100 -> "Moderate Air Quality 🌧️"
        else -> "Unhealthy Air Quality 🌧️"
    }
    val context = LocalContext.current
    var userLat by remember { mutableStateOf<Double?>(null) }
    var userLon by remember { mutableStateOf<Double?>(null) }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationPermissionState  = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    LaunchedEffect(Unit) {
        locationPermissionState.launchMultiplePermissionRequest()
    }

    LaunchedEffect(locationPermissionState.allPermissionsGranted) {
        if (locationPermissionState.allPermissionsGranted) {
            try {
                val location = fusedLocationClient.lastLocation.await()
                location?.let {
                    userLat = it.latitude
                    userLon = it.longitude
                    viewModel.fetchAqi( it.latitude, it.longitude)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    var greeting by remember { mutableStateOf("Good Morning ☀️") }

    LaunchedEffect(Unit) {
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        greeting = when (hour) {
            in 5..11 -> "Good Morning ☀️"
            in 12..17 -> "Good Afternoon 🌤️"
            in 18..22 -> "Good Evening 🌙"
            else -> "Good Night 🌌"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF4FC3F7),
                        Color(0xFF80DEEA),
                        Color(0xFFE0F7FA)
                    )
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().systemBarsPadding(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = greeting,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                    Text(
                        text = "CleanAir Dashboard",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 16.sp
                    )
                }

                IconButton(onClick = {
                    navController.navigate("login") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                }) {
                    Icon(Icons.Default.Logout, contentDescription = null, tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(Icons.Default.Place, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(dummyCity, color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(30.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Air Quality Index",
                        style = MaterialTheme.typography.titleMedium,
                        color = Neutral700
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    if (dummyAQI != null) {
                        AQIGauge(dummyAQI)
                    }else{
                        Text("No Data Available")
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = dummyStatus,
                        color = if ((dummyAQI ?: 0) <= 50) Color(0xFF00C853)
                        else if ((dummyAQI ?: 0) <= 100) Color(0xFFFFC107)
                        else Color(0xFFD32F2F),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    InfoCard(title = "Temperature", value = "$dummyTemp°C", color = Color(0xFF4FC3F7))
                }
                item {
                    InfoCard(title = "Humidity", value = "$dummyHumidity%", color = Color(0xFF81D4FA))
                }
                item {
                    InfoCard(title = "Wind", value = "6.2 km/h", color = Color(0xFF80CBC4))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            AnimatedVisibility(true) {
                Text(
                    text = "\"Clean Air, Clear Mind. Keep Breathing Fresh!\" 🌱",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun InfoCard(title: String, value: String, color: Color) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .width(150.dp)
            .shadow(10.dp, RoundedCornerShape(20.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Cloud, contentDescription = null, tint = color)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, color = Neutral700, fontWeight = FontWeight.Medium)
            Text(text = value, color = color, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}

@Composable
fun AQIGauge(aqi: Int) {
    val color = when {
        aqi <= 50 -> Color(0xFF00C853)
        aqi <= 100 -> Color(0xFFFFC107)
        else -> Color(0xFFD32F2F)
    }
    Box(contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(140.dp)) {
            val sweep = (aqi.coerceIn(0, 500) / 500f) * 270f
            drawArc(
                color = Color.LightGray.copy(alpha = 0.3f),
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(18f)
            )
            drawArc(
                color = color,
                startAngle = 135f,
                sweepAngle = sweep,
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(18f)
            )
        }
        Text(
            text = "$aqi",
            color = color,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
