package org.maternalcare.modules.main.user.model.dto

data class UserImmunizationDto(
    var id: String? = null,
    var userId: String = "",
    var firstDoseGiven: String = "",
    var firstDoseReturn: String = "",
    var secondDoseGiven: String = "",
    var secondDoseReturn: String = "",
    var thirdDoseGiven: String = "",
    var thirdDoseReturn: String = "",
    var fourthDoseGiven: String = "",
    var fourthDoseReturn: String = "",
    var fifthDoseGiven: String = "",
    var fifthDoseReturn: String = "",
    var createdById: String? = null,
    var createdAt: String? = null,
    var lastUpdatedById: String? = null,
    var lastUpdatedAt: String? = null,
    var deletedById: String? = null,
    var deletedAt: String? = null,
)
