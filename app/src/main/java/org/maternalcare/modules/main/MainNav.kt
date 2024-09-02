package org.maternalcare.modules.main

import kotlinx.serialization.Serializable

@Serializable
object MainNav {
    @Serializable
    object Menu

    @Serializable
    data class Addresses(val status: String, var isArchive: Boolean = false)

    @Serializable
    data class Residences(val status: String, var isArchive: Boolean = false)

    @Serializable
    object ChooseCheckup

    @Serializable
    object CheckupDetails

    @Serializable
    object MessagesList

    @Serializable
    object Messages

    @Serializable
    object Dashboard

    @Serializable
    object User

    @Serializable
    object CreateUser

    @Serializable
    data class EditUser(val userId: String)

    @Serializable
    object Settings

    @Serializable
    object EditSettings

    @Serializable
    object MonitoringCheckup

    @Serializable
    object EditCheckup

    @Serializable
    object UserPreview
}
