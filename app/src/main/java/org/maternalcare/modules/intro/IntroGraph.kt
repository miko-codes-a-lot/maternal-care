package org.maternalcare.modules.intro

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import org.maternalcare.modules.intro.login.ForgotPasswordUI
import org.maternalcare.modules.intro.login.LoginUI
import org.maternalcare.modules.intro.login.TokenVerificationUI
import org.maternalcare.modules.intro.login.ResetPasswordUI
import org.maternalcare.modules.intro.splash.MapUI
import org.maternalcare.modules.intro.splash.SplashUI
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.viewmodel.UserViewModel

fun NavGraphBuilder.introGraph(navController: NavController) {
    navigation<IntroNav>(startDestination = IntroNav.Splash) {
        composable<IntroNav.Splash> {
            SplashUI(navController)
//            MapUI()
        }

        composable<IntroNav.Login> {
            LoginUI(userDto = UserDto(), navController)
        }

        composable<IntroNav.ForgotPassword> {
            ForgotPasswordUI(navController)
        }

        composable<IntroNav.TokenVerification> {
            val args = it.toRoute<IntroNav.TokenVerification>()
            val userViewModel: UserViewModel = hiltViewModel()
            val userDto = userViewModel.fetchUserByEmail(args.email)
            if (userDto != null) {
                TokenVerificationUI(email = userDto.email!!, navController = navController)
            }
        }

        composable<IntroNav.ResetPassword> {
            val args = it.toRoute<IntroNav.ResetPassword>()
            val userViewModel: UserViewModel = hiltViewModel()
            val userDto = userViewModel.fetchUserByEmailAndToken(args.email, args.passwordToken)
            if(userDto != null){
                ResetPasswordUI(email = userDto.email!!, token = userDto.resetPasswordToken!!, navController = navController)
            }
        }
    }
}