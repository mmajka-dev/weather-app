package pl.mmajka.weatherapp.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import pl.mmajka.weatherapp.R
import pl.mmajka.weatherapp.ui.theme.WeatherTheme

@Composable
internal fun LoadingContent() {
    val colors = WeatherTheme.colors
    val dimensions = WeatherTheme.dimensions

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier.size(dimensions.iconM),
            strokeWidth = dimensions.strokeWidth,
            color = colors.accentLight
        )
    }
}

@Composable
internal fun ErrorContent(message: String, onRetry: () -> Unit) {
    val colors = WeatherTheme.colors
    val dimensions = WeatherTheme.dimensions

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensions.spacingM),
            modifier = Modifier.padding(dimensions.spacingXxxl)
        ) {
            Text(
                text = stringResource(R.string.weather_error_failed_to_load),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = Color.White
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = colors.muted
            )
            Spacer(modifier = Modifier.height(dimensions.spacingXxs))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.accent,
                    contentColor = Color.White
                )
            ) {
                Text(stringResource(R.string.weather_retry))
            }
        }
    }
}
