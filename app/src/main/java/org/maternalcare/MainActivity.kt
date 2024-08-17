package org.maternalcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import org.maternalcare.ui.intro.IntroNav
import org.maternalcare.ui.intro.introGraph
import org.maternalcare.ui.main.mainGraph
import org.maternalcare.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = IntroNav) {
                    introGraph(navController)
                    mainGraph(navController)
                }
            }
        }
    }
}
