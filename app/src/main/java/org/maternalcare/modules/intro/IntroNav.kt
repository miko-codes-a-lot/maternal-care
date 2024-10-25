package org.maternalcare.modules.intro

import kotlinx.serialization.Serializable

@Serializable
object IntroNav {
    @Serializable
    object Splash

    @Serializable
    object Login

    @Serializable
    object ForgotPassword

    @Serializable
    data class TokenVerification(val email: String)

    @Serializable
    data class ResetPassword(val email: String, val passwordToken: String)

}