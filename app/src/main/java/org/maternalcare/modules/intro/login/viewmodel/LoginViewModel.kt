package org.maternalcare.modules.intro.login.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import org.maternalcare.modules.intro.IntroNav
import org.maternalcare.modules.intro.login.dto.LoginDto
import org.maternalcare.modules.intro.login.service.AuthService
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.service.UserService
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService,
    private val userService: UserService,
    application: Application
): ViewModel()  {
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("Preferences", Context.MODE_PRIVATE)

    fun login(loginDto: LoginDto): Boolean {
        val userDto = authService.login(loginDto)

        if (userDto != null) {
            if (userDto.id == null) return false
            saveUserSession(userDto.id ?: "")
            return true
        }

        return false
    }

    private fun saveUserSession(userId: String) {
        val editor = sharedPreferences.edit()
        editor.putString("logged_in_user_id", userId)
        editor.apply()
    }

    fun getLoggedInUserId(): String? {
        return sharedPreferences.getString("logged_in_user_id", null)
    }

    fun getUserLocal(): UserDto? {
        val userId = getLoggedInUserId()

        if (userId != null){
            return userService.fetchOne(userId)
        }

        return null
    }

    fun logout(navController: NavController) {
        sharedPreferences.edit().remove("logged_in_user_id").apply()
        navController.navigate(IntroNav)

    }
}