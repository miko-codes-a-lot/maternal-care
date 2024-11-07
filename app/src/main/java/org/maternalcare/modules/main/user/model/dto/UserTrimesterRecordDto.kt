package org.maternalcare.modules.main.user.model.dto

data class UserTrimesterRecordDto(
    var id: String? = null,
    var trimesterUserId: String = "",
    var pregnancyUserId: String = "",
    var trimesterOrder: Int = 0,
)