package org.maternalcare.ui.main

import kotlinx.serialization.Serializable

@Serializable
object MainNav {
    @Serializable
    object Menu

    @Serializable
    object Residence {
        @Serializable
        object Address
    }
}
