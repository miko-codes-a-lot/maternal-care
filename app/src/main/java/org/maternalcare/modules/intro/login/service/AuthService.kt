package org.maternalcare.modules.intro.login.service

import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import org.maternalcare.modules.intro.login.model.dto.LoginDto
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.model.entity.User
import org.maternalcare.modules.main.user.model.mapper.toDTO
import org.maternalcare.shared.ext.verifyPassword
import javax.inject.Inject

class AuthService @Inject constructor(
    private val realm: Realm,
    private val emailService: EmailService
) {
    fun login(loginDto: LoginDto): UserDto? {
        val query = StringBuilder()
            .append("email == $0")
            .append(" AND isActive == true")
            .append(" AND isArchive == false")

        val user = realm.query<User>(query.toString(), loginDto.username)
            .find()
            .firstOrNull()

        if (user == null || !user.password.verifyPassword(loginDto.password)) {
            return null
        }

        return user.toDTO()
    }
    suspend fun requestPasswordReset(email: String): Boolean {
        return emailService.requestPasswordResetToken(email)
    }

    suspend fun verifyToken(email: String, token: String): Boolean {
        return emailService.verifyResetToken(email, token)
    }
}