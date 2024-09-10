package org.maternalcare.modules.intro.login.viewmodel

import android.annotation.SuppressLint
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay

class UserStateViewModel : ViewModel() {
    var isLoggedIn by mutableStateOf(false)
    var isBusy by mutableStateOf(false)

    suspend fun signIn() {
        isBusy = true
        delay(3000)
        isLoggedIn = true
        isBusy = false
    }

    suspend fun signOut() {
        isBusy = true
        delay(3000)
        isLoggedIn = false
        isBusy = false
    }
}
@SuppressLint("CompositionLocalNaming")
val UserState = compositionLocalOf<UserStateViewModel> { error("User State Context Not Found!") }