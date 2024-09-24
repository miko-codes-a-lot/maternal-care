package org.maternalcare.modules.main

import kotlinx.serialization.Serializable

@Serializable
object MainNav {
    @Serializable
    object Menu

    @Serializable
    data class Addresses(val status: String, var isArchive: Boolean = false)

    @Serializable
    data class Residences(
        val status: String,
        var isArchive: Boolean = false,
        var addressId: String? = null,
        var dateOfCheckup: String? = null
    )

    @Serializable
    data class ResidencePreview(val userId: String)

    @Serializable
    object Reminders

    @Serializable
    data class ChooseCheckup(val userId: String)

    @Serializable
    data class CheckupDetails(val userId: String, val checkupNumber: Int)

    @Serializable
    data class CheckupDetailsEdit(val userId: String, val checkupId: String, val checkupNumber: Int)

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
    data class CreateUser(val addressId: String?)

    @Serializable
    data class EditUser(val userId: String)

    @Serializable
    object Settings

    @Serializable
    object EditSettings

    @Serializable
    data class MonitoringCheckup(val isComplete: Boolean)

    @Serializable
    data class EditCheckup(val checkUpId: String)

    @Serializable
    object UserPreview
}
