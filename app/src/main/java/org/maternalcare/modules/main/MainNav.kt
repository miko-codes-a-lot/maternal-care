package org.maternalcare.modules.main

import kotlinx.serialization.Serializable

@Serializable
object MainNav {
    @Serializable
    object Menu

    @Serializable
    data class Addresses(val status: String)

    @Serializable
    data class Residences(val status: String)

    @Serializable
    object ChooseCheckup

    @Serializable
    object CheckupDetails

    @Serializable
    object Dashboard

    @Serializable
    object User

    @Serializable
    object Settings
}
