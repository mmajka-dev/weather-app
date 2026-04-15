package pl.mmajka.weatherapp.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import pl.mmajka.weatherapp.ui.details.WeatherDetailsScreen
import pl.mmajka.weatherapp.ui.search.SearchScreen

private const val NAV_TRANSITION_DURATION = 300

@Composable
fun WeatherNavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        startDestination = SearchRoute
    ) {
        composable<SearchRoute>(
            exitTransition = { fadeOut(tween(NAV_TRANSITION_DURATION)) },
            popEnterTransition = { fadeIn(tween(NAV_TRANSITION_DURATION)) }
        ) {
            SearchScreen(
                onNavigateToDetails = { city ->
                    navController.navigate(city.toRoute())
                }
            )
        }

        composable<WeatherDetailsRoute>(
            enterTransition = { fadeIn(tween(NAV_TRANSITION_DURATION)) },
            popExitTransition = { fadeOut(tween(NAV_TRANSITION_DURATION)) }
        ) { entry ->
            val city = entry.toRoute<WeatherDetailsRoute>().toCity()
            WeatherDetailsScreen(
                city = city,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
