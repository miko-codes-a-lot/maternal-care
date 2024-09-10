package org.maternalcare.modules.intro.login.service

import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import org.maternalcare.modules.intro.login.model.dto.LoginDto
import org.maternalcare.modules.main.user.model.entity.User
import org.maternalcare.modules.main.user.model.mapper.toDTO
import org.maternalcare.shared.ext.verifyPassword
import javax.inject.Inject

class AuthService @Inject constructor(private val realm: Realm) {
    fun login(loginDto: LoginDto): Boolean {
        val user = realm.query<User>("email == $0", loginDto.username)
            .find()
            .firstOrNull()
            ?.run { toDTO() }

        if (user == null) return false

        return user.password.verifyPassword(loginDto.password)
    }
}