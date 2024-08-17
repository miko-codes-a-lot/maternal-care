package org.maternalcare.modules.intro

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.maternalcare.modules.intro.login.LoginUI
import org.maternalcare.modules.intro.splash.SplashUI

fun NavGraphBuilder.introGraph(navController: NavController) {
    navigation<IntroNav>(startDestination = IntroNav.Splash) {
        composable<IntroNav.Splash> {
            SplashUI(navController)
        }

        composable<IntroNav.Login> {
            LoginUI(navController)
        }
    }
}