package org.maternalcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import org.maternalcare.modules.intro.IntroNav
import org.maternalcare.modules.intro.UserState
import org.maternalcare.modules.intro.UserStateViewModel
import org.maternalcare.modules.intro.introGraph
import org.maternalcare.modules.main.mainGraph
import org.maternalcare.ui.theme.AppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val userState by viewModels<UserStateViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navController = rememberNavController()
                CompositionLocalProvider(UserState provides userState) {
                    NavHost(navController = navController, startDestination = IntroNav) {
                        introGraph(navController)
                        mainGraph(navController)
                    }
                }
            }
        }
    }
}
