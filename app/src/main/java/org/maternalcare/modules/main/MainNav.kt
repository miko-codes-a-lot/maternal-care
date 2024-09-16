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
    data class CheckupDetails(val checkUpId: String)

    @Serializable
    object MessagesList

    @Serializable
    data class Messages(val userId: String)

    @Serializable
    object ReminderLists

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
    data class EditCheckup(val checkUpId: String)

    @Serializable
    object UserPreview
}
