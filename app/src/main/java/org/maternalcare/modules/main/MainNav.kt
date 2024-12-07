package org.maternalcare.modules.main

import kotlinx.serialization.Serializable

@Serializable
object MainNav {
    @Serializable
    object Menu

    @Serializable
    data class Addresses(val status: String, val isArchive: Boolean = false)

    @Serializable
    data class Residences(
        val status: String,
        var isArchive: Boolean = false,
        var addressId: String? = null,
        var dateOfCheckUp: String? = null,
        var isDashboard: Boolean
    )

    @Serializable
    data class ResidencePreview(val userId: String)

    @Serializable
    object Reminders

    @Serializable
    data class ChooseCheckup(val userId: String, val pregnantRecordId: String, val pregnantTrimesterId: String)

    @Serializable
    data class CheckupDetails(val userId: String, val checkupNumber: Int, val pregnantRecordId: String, val pregnantTrimesterId: String)

    @Serializable
    data class CheckupDetailsEdit(val userId: String, val checkupId: String, val checkupNumber: Int, val pregnantRecordId: String, val pregnantTrimesterId: String)

    @Serializable
    data class ConditionStatus(val userId: String)

    @Serializable
    data class StatusPreview(val userId: String)

    @Serializable
    data class ImmunizationRecord(val userId: String, val pregnantRecordId: String)

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
    data class MonitoringCheckup(val isComplete: Boolean, val dashboard: Boolean, val isArchive: Boolean)

    @Serializable
    data class HealthRecord(val userId: String)

    @Serializable
    data class CreateRecord(val userId: String)

    @Serializable
    data class CreateTrimester(val userId: String, val pregnantRecordId: String)

    @Serializable
    data class TrimesterCheckUpList(val userId: String, val pregnantRecordId: String, val trimesterId: String)

    @Serializable
    data class Map(var addressId: String)

    @Serializable
    object ChatLobby

    @Serializable
    data class ChatDirect(val userId: String)

    @Serializable
    object AboutView

    @Serializable
    object UserManual

    @Serializable
    object BhwManual

    @Serializable
    object MidWifeManual

    @Serializable
    object PregnantManual

}
