package org.maternalcare.modules.main.user.model

data class UserCheckup(
    var id: String = "",
    var userId: String = "",
    var height: Double = 0.0,
    var weight: Double = 0.0,
    var checkup: Int = 1,
    var lastMenstrualPeriod: String = ""
)