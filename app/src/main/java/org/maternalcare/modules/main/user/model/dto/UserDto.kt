package org.maternalcare.modules.main.user.model.dto

data class UserDto(
    var id: String? = null,
    var firstName: String = "",
    var middleName: String? = null,
    var lastName: String = "",
    var email: String? = null,
    var mobileNumber: String? = null,
    var dateOfBirth: String = "",
    var password: String = "",
    var createdBy: String? = null,
    var createdAt: String? = null,
    var lastUpdatedBy: String? = null,
    var lastUpdatedAt: String? = null,
    var deletedBy: String? = null,
    var deletedAt: String? = null,
    var isActive: Boolean = true,
    var isSuperAdmin: Boolean = false,
    var isAdmin: Boolean = false,
    var isResidence: Boolean = true,
)
