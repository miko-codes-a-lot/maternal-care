package org.maternalcare

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

fun NavGraphBuilder.introGraph(navController: NavController) {
    navigation<IntroRoute>(startDestination = IntroRoute.Splash) {
        composable<IntroRoute.Splash> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    navController.navigate(IntroRoute.Login)
                }) {
                    Text(text = "Get Started")
                }
            }
        }

        composable<IntroRoute.Login> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    navController.navigate(MainRoute.Menu)
                }) {
                    Text(text = "Login")
                }
            }
        }
    }
}

fun NavGraphBuilder.mainGraph(navController: NavController) {
    navigation<MainRoute>(startDestination = MainRoute.Menu) {
        composable<MainRoute.Menu> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Address 1")
                }
            }
        }
    }
}

@Serializable
object IntroRoute {
    @Serializable
    object Splash

    @Serializable
    object Login
}

@Serializable
object MainRoute {
    @Serializable
    object Menu

    @Serializable
    object Residence {
        @Serializable
        object Address
    }
}
