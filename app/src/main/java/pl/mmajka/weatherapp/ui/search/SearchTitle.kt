package pl.mmajka.weatherapp.ui.search

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.mmajka.weatherapp.R
import pl.mmajka.weatherapp.ui.theme.WeatherTheme

@Composable
internal fun AnimatedTitle(modifier: Modifier = Modifier) {
    val colors = WeatherTheme.colors
    val dimensions = WeatherTheme.dimensions

    val visible = rememberDelayedVisibility(TITLE_DELAY)

    val offsetY by animateFloatAsState(
        targetValue = if (visible) 0f else -30f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "titleOffset"
    )
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(600),
        label = "titleAlpha"
    )

    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.search_title),
            color = Color.White,
            fontSize = TITLE_FONT_SIZE.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .offset(y = offsetY.dp)
                .alpha(alpha)
        )
        Spacer(modifier = Modifier.height(dimensions.spacingS))
        Text(
            text = stringResource(R.string.search_subtitle),
            color = colors.muted,
            fontSize = SUBTITLE_FONT_SIZE.sp,
            modifier = Modifier.alpha(alpha)
        )
    }
}
