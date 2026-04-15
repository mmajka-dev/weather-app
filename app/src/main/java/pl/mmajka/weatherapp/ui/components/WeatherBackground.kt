package pl.mmajka.weatherapp.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.mmajka.weatherapp.ui.theme.WeatherTheme
import kotlin.random.Random

private const val PARTICLE_MIN_DURATION = 20_000
private const val PARTICLE_MAX_DURATION = 40_000
private const val DEFAULT_PARTICLE_COUNT = 20
private const val PARTICLE_RADIUS_DP = 5f
private const val ACCENT_CIRCLE_RADIUS_DP = 280f
private const val ACCENT_CIRCLE_OFFSET_Y_DP = 70f
private const val CYAN_CIRCLE_RADIUS_DP = 240f
private const val CYAN_CIRCLE_OFFSET_X_DP = 55f

private data class Particle(
    val id: Int,
    val startFractionX: Float,
    val startFractionY: Float,
    val targetFractionX: Float,
    val targetFractionY: Float,
    val duration: Int,
)

@Composable
fun WeatherBackground() {
    BlurredCircleBackground()
    FloatingParticles()
}

@Composable
private fun rememberParticles(count: Int): List<Particle> = remember {
    List(count) { i ->
        Particle(
            id = i,
            startFractionX = Random.nextFloat(),
            startFractionY = Random.nextFloat(),
            targetFractionX = Random.nextFloat(),
            targetFractionY = Random.nextFloat(),
            duration = (PARTICLE_MIN_DURATION..PARTICLE_MAX_DURATION).random(),
        )
    }
}

@Composable
private fun FloatingParticles(count: Int = DEFAULT_PARTICLE_COUNT) {
    val colors = WeatherTheme.colors
    val particles = rememberParticles(count)
    val transition = rememberInfiniteTransition(label = "particles")

    val animatedPositions = particles.map { p ->
        val x = transition.animateFloat(
            initialValue = p.startFractionX,
            targetValue = p.targetFractionX,
            animationSpec = infiniteRepeatable(
                tween(p.duration, easing = LinearEasing),
                RepeatMode.Reverse
            ),
            label = "px_${p.id}",
        )
        val y = transition.animateFloat(
            initialValue = p.startFractionY,
            targetValue = p.targetFractionY,
            animationSpec = infiniteRepeatable(
                tween(p.duration, easing = LinearEasing),
                RepeatMode.Reverse
            ),
            label = "py_${p.id}",
        )
        x to y
    }

    val particleColor = colors.accentLight.copy(alpha = 0.2f)

    Canvas(modifier = Modifier.fillMaxSize()) {
        val radiusPx = PARTICLE_RADIUS_DP.dp.toPx()
        animatedPositions.forEach { (x, y) ->
            drawCircle(
                color = particleColor,
                radius = radiusPx,
                center = Offset(x.value * size.width, y.value * size.height),
            )
        }
    }
}

@Composable
private fun BlurredCircleBackground() {
    val colors = WeatherTheme.colors

    Canvas(modifier = Modifier.fillMaxSize()) {
        val accentRadius = ACCENT_CIRCLE_RADIUS_DP.dp.toPx()
        val accentCenter = Offset(size.width * 0.8f, -ACCENT_CIRCLE_OFFSET_Y_DP.dp.toPx())
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    colors.accent.copy(0.15f),
                    colors.accent.copy(0.08f),
                    Color.Transparent
                ),
                center = accentCenter,
                radius = accentRadius,
            ),
            radius = accentRadius,
            center = accentCenter,
        )
        val cyanRadius = CYAN_CIRCLE_RADIUS_DP.dp.toPx()
        val cyanCenter = Offset(-CYAN_CIRCLE_OFFSET_X_DP.dp.toPx(), size.height * 0.85f)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    colors.cyan.copy(0.12f),
                    colors.cyan.copy(0.06f),
                    Color.Transparent
                ),
                center = cyanCenter,
                radius = cyanRadius,
            ),
            radius = cyanRadius,
            center = cyanCenter,
        )
    }
}
