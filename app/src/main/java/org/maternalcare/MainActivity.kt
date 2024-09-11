package org.maternalcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import org.maternalcare.modules.intro.IntroNav
import org.maternalcare.modules.intro.introGraph
import org.maternalcare.modules.intro.login.viewmodel.LoginViewModel
import org.maternalcare.modules.intro.login.viewmodel.UserState
import org.maternalcare.modules.intro.login.viewmodel.UserStateViewModel
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.mainGraph
import org.maternalcare.ui.theme.AppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val userState by viewModels<UserStateViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val loginViewModel: LoginViewModel = hiltViewModel()

            AppTheme {
                val navController = rememberNavController()
                CompositionLocalProvider(UserState provides userState) {
                    val startingDestination = getStartDestination(loginViewModel)
                    NavHost(navController = navController, startDestination = startingDestination) {
                        introGraph(navController)
                        mainGraph(navController)
                    }
                }
            }
        }
    }

    private fun getStartDestination(loginViewModel: LoginViewModel): Any {
        if (loginViewModel.getLoggedInUserId() != null){
            return MainNav
        }
        return IntroNav
    }
}
