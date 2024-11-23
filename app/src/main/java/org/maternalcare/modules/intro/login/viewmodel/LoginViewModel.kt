package org.maternalcare.modules.intro.login.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.maternalcare.modules.intro.IntroNav
import org.maternalcare.modules.intro.login.model.dto.LoginDto
import org.maternalcare.modules.intro.login.service.AuthService
import org.maternalcare.modules.intro.login.service.EmailService
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.service.UserService
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService,
    private val userService: UserService,
    private val emailService: EmailService,
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

    private fun clearUserSession() {
        sharedPreferences.edit().clear().apply()
    }

    fun logout(navController: NavController) {
        val userId = getLoggedInUserId() 
        if (userId != null) {
            clearUserSession()
            navController.navigate(IntroNav) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    fun sendPasswordResetToken(email: String, callback: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val success = authService.requestPasswordReset(email)
                if (success) {
                    callback(true, null)
                } else {
                    Log.e("ForgotPassword", "Failed to send password reset token for email: $email")
                    callback(false, "Failed to send password reset token.")
                }
            } catch (e: Exception) {
                callback(false, "Error: ${e.message}")
            }
        }
    }

    fun verifyToken(email: String, token: String, callback: (Boolean, String?) -> Unit) {
        if (email.isBlank() || token.isBlank()) {
            callback(false, "Email and token cannot be blank.")
            return
        }
        viewModelScope.launch {
            try {
                val success = authService.verifyToken(email, token)
                if (success) {
                    callback(true, null)
                } else {
                    callback(false, "Token verification failed.")
                }
            } catch (e: Exception) {
                callback(false, "Failed to verify token: ${e.localizedMessage}")
            }
        }
    }

    fun resetPassword(email: String, token: String, newPassword: String, callback: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val success = userService.saveNewPassword(email, token, newPassword)
                if (success) {
                    callback(true, null)
                } else {
                    callback(false, "Failed to reset password. Invalid token or email.")
                }
            } catch (e: Exception) {
                callback(false, "Error resetting password: ${e.localizedMessage}")
            }
        }
    }
}