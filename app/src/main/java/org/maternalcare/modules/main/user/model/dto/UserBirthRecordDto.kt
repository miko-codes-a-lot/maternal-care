package org.maternalcare.modules.main.user.model.dto

data class UserBirthRecordDto(
    var id: String? = null,
    var pregnancyUserId: String = "",
    var childOrder: Int = 0,
    var pregnancyOrder: Int = 0,
    var fillDate: String = "",
)