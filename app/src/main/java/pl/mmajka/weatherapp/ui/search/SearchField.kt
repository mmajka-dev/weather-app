package pl.mmajka.weatherapp.ui.search

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import pl.mmajka.weatherapp.R
import pl.mmajka.weatherapp.ui.theme.WeatherTheme

@Composable
internal fun GlassSearchField(
    value: String,
    onValueChange: (String) -> Unit
) {
    val colors = WeatherTheme.colors
    val dimensions = WeatherTheme.dimensions
    var isFocused by remember { mutableStateOf(false) }

    val glowAlpha by animateFloatAsState(
        targetValue = if (isFocused) 0.1f else 0f,
        animationSpec = tween(300),
        label = "focusGlow"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensions.searchFieldHeight)
    ) {
        if (glowAlpha > 0) {
            Canvas(modifier = Modifier.matchParentSize()) {
                drawRoundRect(
                    color = colors.accent.copy(alpha = glowAlpha),
                    cornerRadius = CornerRadius(dimensions.radiusLarge.toPx())
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.08f),
                            Color.White.copy(alpha = 0.05f)
                        )
                    ),
                    shape = RoundedCornerShape(dimensions.radiusLarge)
                )
                .border(
                    width = dimensions.borderWidth,
                    color = Color.White.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(dimensions.radiusLarge)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensions.spacingXl),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = colors.muted,
                    modifier = Modifier.size(dimensions.iconS)
                )
                Spacer(modifier = Modifier.width(dimensions.spacingM))
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { isFocused = it.isFocused },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                    singleLine = true,
                    cursorBrush = SolidColor(Color.White),
                    decorationBox = { innerTextField ->
                        if (value.isEmpty()) {
                            Text(
                                text = stringResource(R.string.search_placeholder),
                                style = MaterialTheme.typography.bodyLarge,
                                color = colors.placeholder
                            )
                        }
                        innerTextField()
                    }
                )
            }
        }
    }
}
