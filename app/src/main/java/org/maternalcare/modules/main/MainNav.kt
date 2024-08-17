package org.maternalcare.modules.main

import kotlinx.serialization.Serializable

@Serializable
object MainNav {
    @Serializable
    object Menu

    @Serializable
    object Residence {
        @Serializable
        object Addresses

        @Serializable
        object Residences

        @Serializable
        object ChooseCheckup

        @Serializable
        object CheckupDetails
    }
}
