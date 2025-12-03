package uk.ac.tees.mad.cleanair.ui.screens.dashboard

import android.Manifest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.cleanair.ui.theme.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Observing ViewModel data
    val aqi by viewModel.aqi.collectAsState()
    val forecast by viewModel.forecast.collectAsState()

    val temp = forecast?.hourly?.temperature_2m?.firstOrNull() ?: 0
    val humidity = forecast?.hourly?.relative_humidity_2m?.firstOrNull() ?: 0
    val wind = forecast?.hourly?.wind_speed_10m?.firstOrNull() ?: 0

    val statusColor = when {
        (aqi ?: 0) <= 50 -> GoodGreen
        (aqi ?: 0) <= 100 -> ModerateYellow
        (aqi ?: 0) <= 150 -> UnhealthyOrange
        else -> VeryUnhealthyRed
    }

    val statusText = when {
        (aqi ?: 0) <= 50 -> "Good Air Quality"
        (aqi ?: 0) <= 100 -> "Moderate Air Quality"
        else -> "Unhealthy Air Quality"
    }

    val fusedClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val permissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    LaunchedEffect(Unit) {
        permissionState.launchMultiplePermissionRequest()
    }

    LaunchedEffect(permissionState.allPermissionsGranted) {
        if (permissionState.allPermissionsGranted) {
            val loc = fusedClient.lastLocation.await()
            loc?.let {
                viewModel.fetchAqi(it.latitude, it.longitude)
                viewModel.fetchForecast(it.latitude, it.longitude)
            }
        }
    }

    var greeting by remember { mutableStateOf("Hello") }
    LaunchedEffect(Unit) {
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        greeting = when (hour) {
            in 5..11 -> "Good Morning ☀️"
            in 12..17 -> "Good Afternoon 🌤️"
            in 18..22 -> "Good Evening 🌙"
            else -> "Good Night 🌌"
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(aqi) {
        if ((aqi ?: 0) > 100) {
            snackbarHostState.showSnackbar(
                "⚠️ Air quality is unhealthy. Avoid outdoor activity.",
                withDismissAction = true
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        val loc = fusedClient.lastLocation.await()
                        loc?.let {
                            viewModel.fetchAqi(it.latitude, it.longitude)
                            viewModel.fetchForecast(it.latitude, it.longitude)
                        }
                    }
                },
                containerColor = White,
                contentColor = DeepSky
            ) {
                Icon(Icons.Outlined.Refresh, contentDescription = "Refresh")
            }
        },
        containerColor = Color.Transparent
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(LightSky, SkyBlue, DeepSky)
                    )
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding(),
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
                            color = White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                        Text(
                            text = "CleanAir Dashboard",
                            color = White.copy(alpha = 0.95f),
                            fontSize = 16.sp
                        )
                    }

                    IconButton(onClick = {
                        navController.navigate("login") {
                            popUpTo("dashboard") { inclusive = true }
                        }
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout",
                            tint = White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(28.dp)),
                    colors = CardDefaults.cardColors(containerColor = White)
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

                        if (aqi != null) {
                            AQIGauge(aqi = aqi!!, indicatorColor = statusColor)
                        } else {
                            CircularProgressIndicator(
                                modifier = Modifier.size(36.dp),
                                color = SkyBlue
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "$statusText • ${(aqi ?: 0)}",
                            color = statusColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item { InfoCard("Temperature", "$temp°C", SkyBlue) }
                    item { InfoCard("Humidity", "$humidity%", AquaTeal) }
                    item { InfoCard("Wind", "$wind km/h", DeepSky) }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    QuickNavButton("Health Tips", SkyBlue) { navController.navigate("healthTips/${aqi ?: 0}")}
                    QuickNavButton("Log Symptoms", SkyBlue) {
                        navController.navigate("logSymptoms")
                    }
                    QuickNavButton("Profile", DeepSky) { navController.navigate("profile") }
                }

                Spacer(modifier = Modifier.height(24.dp))

                AnimatedVisibility(true) {
                    Text(
                        text = "\"Every breath matters.\"",
                        color = White.copy(alpha = 0.95f),
                        fontSize = 14.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
fun QuickNavButton(title: String, accent: Color, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White.copy(alpha = 0.95f)),
        modifier = Modifier
            .height(64.dp)
            .width(116.dp)
            .clickable { onClick() }
            .shadow(6.dp, RoundedCornerShape(16.dp))
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(title, color = accent, fontWeight = FontWeight.Medium, fontSize = 14.sp)
        }
    }
}

@Composable
fun InfoCard(title: String, value: String, color: Color) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        modifier = Modifier
            .width(150.dp)
            .shadow(10.dp, RoundedCornerShape(18.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.18f)),
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
fun AQIGauge(aqi: Int, indicatorColor: Color) {
    Box(contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(150.dp)) {
            val sweep = (aqi.coerceIn(0, 500) / 500f) * 270f
            drawArc(
                color = Neutral200,
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(18f)
            )
            drawArc(
                color = indicatorColor,
                startAngle = 135f,
                sweepAngle = sweep,
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(18f)
            )
        }
        Text(
            text = "$aqi",
            color = indicatorColor,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
