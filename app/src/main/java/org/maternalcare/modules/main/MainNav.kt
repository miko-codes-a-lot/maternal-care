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
    object Dashboard

    @Serializable
    object User

    @Serializable
    object Settings
    @Serializable
    object MonitoringCheckup

    @Serializable
    object EditCheckup
}
