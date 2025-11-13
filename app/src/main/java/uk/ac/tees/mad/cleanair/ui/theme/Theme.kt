package uk.ac.tees.mad.cleanair.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import uk.ac.tees.mad.cleanair.ui.theme.Typography

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

val SkyBlue = Color(0xFF2EA8F6)
val DeepSky = Color(0xFF0B7BC1)
val LightSky = Color(0xFFE9F7FF)
val AquaTeal = Color(0xFF0FCFC0)


val White = Color(0xFFFFFFFF)
val Surface = Color(0xFFF7FBFF)
val Neutral100 = Color(0xFFF2F7FA)
val Neutral200 = Color(0xFFDDEFF6)
val Neutral700 = Color(0xFF374151)

val GoodGreen = Color(0xFF2ECC71)
val ModerateYellow = Color(0xFFF1C40F)
val UnhealthyOrange = Color(0xFFF39C12)
val VeryUnhealthyRed = Color(0xFFEA4335)
val HazardPurple = Color(0xFF7C3AED)

val BookmarkAccent = Color(0xFFFFA726)

private val LightColorScheme = lightColorScheme(
    primary = SkyBlue,
    onPrimary = Color.White,
    secondary = AquaTeal,
    background = Surface,
    surface = White,
    onBackground = Neutral700,
    error = VeryUnhealthyRed
)

@Composable
fun CleanAirTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}
