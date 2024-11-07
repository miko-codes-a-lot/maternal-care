package org.maternalcare.modules.main.user.model.dto

data class UserImmunizationDto(
    var id: String? = null,
    var userId: String = "",
    var pregnantRecordId: String = "",
    var firstDoseGiven: String? = null,
    var firstDoseReturn: String? = null,
    var secondDoseGiven: String? = null,
    var secondDoseReturn: String? = null,
    var thirdDoseGiven: String? = null,
    var thirdDoseReturn: String? = null,
    var fourthDoseGiven: String? = null,
    var fourthDoseReturn: String? = null,
    var fifthDoseGiven: String? = null,
    var fifthDoseReturn: String? = null,
    var createdById: String? = null,
    var createdAt: String? = null,
    var lastUpdatedById: String? = null,
    var lastUpdatedAt: String? = null,
    var deletedById: String? = null,
    var deletedAt: String? = null,
)
