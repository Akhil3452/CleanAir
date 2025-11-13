package uk.ac.tees.mad.cleanair.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import uk.ac.tees.mad.cleanair.ui.theme.DeepSky
import uk.ac.tees.mad.cleanair.ui.theme.LightSky
import uk.ac.tees.mad.cleanair.R


@Composable
fun SplashScreen(navController: NavController, onTimeoutRoute: String = "auth") {
    val infinite = rememberInfiniteTransition()
    val cloudOffset1 = infinite.animateFloat(
        initialValue = -120f,
        targetValue = 600f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val cloudOffset2 = infinite.animateFloat(
        initialValue = 600f,
        targetValue = -120f,
        animationSpec = infiniteRepeatable(
            animation = tween(16000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    var startAnim by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(targetValue = if (startAnim) 1f else 0.85f, animationSpec = tween(800))
    val alpha by animateFloatAsState(targetValue = if (startAnim) 1f else 0f, animationSpec = tween(900))

    LaunchedEffect(Unit) {
        startAnim = true
        delay(2000)
//        navController.navigate(onTimeoutRoute) {
//            popUpTo("splash") { inclusive = true }
//        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(listOf(LightSky, DeepSky))
            )
    ) {
        Box(
            modifier = Modifier
                .offset(x = cloudOffset1.value.dp, y = 40.dp)
                .size(120.dp, 60.dp)
                .align(Alignment.TopStart)
                .alpha(0.35f)
                .background(Color.White.copy(alpha = 0.4f), shape = CircleShape)
        )

        Box(
            modifier = Modifier
                .offset(x = cloudOffset2.value.dp, y = 80.dp)
                .size(140.dp, 70.dp)
                .align(Alignment.TopEnd)
                .alpha(0.28f)
                .background(Color.White.copy(alpha = 0.35f), shape = CircleShape)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.app),
                contentDescription = "CleanAir Logo",
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .scale(scale)
                    .alpha(alpha)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Breathe Better. Live Better.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(alpha)
            )
        }
    }
}

