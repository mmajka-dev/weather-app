package pl.mmajka.weatherapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import pl.mmajka.weatherapp.ui.theme.WeatherTheme

private const val GLASS_BG_ALPHA_TOP = 0.08f
private const val GLASS_BG_ALPHA_BOTTOM = 0.05f
private const val GLASS_BORDER_ALPHA_TOP = 0.2f
private const val GLASS_BORDER_ALPHA_BOTTOM = 0.1f
private const val GLASS_CARD_ALPHA = 0.3f

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    val colors = WeatherTheme.colors
    val dim = WeatherTheme.dimensions
    val interactionSource = remember { MutableInteractionSource() }
    val cardShape = RoundedCornerShape(dim.radiusLarge)

    Box(
        modifier = modifier.then(
            if (onClick != null) Modifier.clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            else Modifier
        )
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.White.copy(GLASS_BG_ALPHA_TOP),
                            Color.White.copy(GLASS_BG_ALPHA_BOTTOM)
                        )
                    ),
                    cardShape,
                )
                .background(colors.surface.copy(alpha = GLASS_CARD_ALPHA), cardShape)
                .border(
                    dim.borderWidth,
                    Brush.verticalGradient(
                        listOf(
                            Color.White.copy(GLASS_BORDER_ALPHA_TOP),
                            Color.White.copy(GLASS_BORDER_ALPHA_BOTTOM)
                        )
                    ),
                    cardShape
                )
        )
        Box(modifier = Modifier.padding(dim.spacingXl)) { content() }
    }
}
