package org.maternalcare.modules.main.user.model.dto

data class UserCheckupDto(
    var id: String? = null,
    var userId: String = "",
    var bloodPressure: Double = 0.0,
    var height: Double = 0.0,
    var weight: Double = 0.0,
    var lastMenstrualPeriod: String = "",
    var dateOfCheckUp: String = "",
    var scheduleOfNextCheckUp: String = "",
    var checkup: Int = 1,
    var createdById: String? = null,
    var createdAt: String? = null,
    var lastUpdatedById: String? = null,
    var lastUpdatedAt: String? = null,
    var deletedById: String? = null,
    var deletedAt: String? = null,
)