package org.maternalcare.modules.main.user.model

data class UserDto(
    var id: String = "",
    var firstName: String = "",
    var middleName: String = "",
    var lastName: String = "",
    var email: String = "",
    var mobileNumber: String = "",
    var dateOfBirth: String = "",
    var password: String = "",
    var createdBy: String = "",
    var createdAt: String = "",
    var lastUpdatedBy: String = "",
    var lastUpdatedAt: String = "",
    var deletedBy: String = "",
    var deletedAt: String = "",
    var isActive: Boolean = true,
    var isSuperAdmin: Boolean = false,
    var isAdmin: Boolean = false,
    var isResidence: Boolean = true,
)
