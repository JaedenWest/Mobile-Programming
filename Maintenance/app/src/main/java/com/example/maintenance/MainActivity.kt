package com.example.maintenance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.maintenance.ui.theme.MaintenanceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaintenanceTheme {
                MaintenanceAppNavigation()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun MaintenanceAppNavigation() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach { destination ->
                item(
                    icon = {
                        Icon(
                            painterResource(destination.icon),
                            contentDescription = destination.label
                        )
                    },
                    label = { Text(destination.label) },
                    selected = currentRoute == destination.route,
                    onClick = {
                        navController.navigate(destination.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            //avoid having multiple copies of the same destination
                            launchSingleTop = true
                            //restore state when selecting a previously selected tab
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) {
        //hosts the screen content
        NavHost(
            navController = navController,
            startDestination = AppDestinations.HOME.route
            ){
            composable(AppDestinations.HOME.route){
                Greeting("Home Screen")
            }
            composable(AppDestinations.FAVORITES.route){
                Greeting("Favorites Screen")
            }
            composable(AppDestinations.PROFILE.route){
                Greeting("Profile Screen")
            }
        }
    }
}

enum class AppDestinations(
    val route: String,
    val label: String,
    val icon: Int,
    ) {
    HOME("home","Home", R.drawable.ic_home),
    FAVORITES("favorites","Favorites",R.drawable.ic_favorite),
    PROFILE("profile","Profile", R.drawable.ic_account_box),;
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MaintenanceTheme {
        Greeting("Android")
    }
}

@Composable
fun SingleItemSelection(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun MaintenanceList(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun Tires(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}