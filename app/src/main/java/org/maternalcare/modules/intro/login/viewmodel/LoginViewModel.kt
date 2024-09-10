package org.maternalcare.modules.intro.login.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.maternalcare.modules.intro.login.model.dto.LoginDto
import org.maternalcare.modules.intro.login.service.AuthService
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService
): ViewModel()  {
    fun login(loginDto: LoginDto): Boolean {
        return this.authService.login(loginDto)
    }
}