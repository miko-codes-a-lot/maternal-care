package org.maternalcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.maternalcare.ui.login.LoginRoute
import org.maternalcare.ui.login.LoginScreen
import org.maternalcare.ui.splash.SplashRoute
import org.maternalcare.ui.splash.SplashScreen
import org.maternalcare.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = SplashRoute
                ) {
                    composable<SplashRoute> {
                        SplashScreen(navController)
                    }
                    composable<LoginRoute> {
                        LoginScreen()
                    }
                }
            }
        }
    }
}
