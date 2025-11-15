package uk.ac.tees.mad.cleanair.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import uk.ac.tees.mad.cleanair.ui.theme.DeepSky
import uk.ac.tees.mad.cleanair.ui.theme.LightSky

@Composable
fun AuthBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(LightSky, DeepSky)
                )
            )
    ) {
        content()
    }
}
