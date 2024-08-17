package org.maternalcare.modules.main.user.model

data class User(
    var id: String = "",
    var firstName: String = "",
    var middleName: String = "",
    var email: String = "",
    var mobileNumber: String = "",
    var password: String = "",
    var isActive: Boolean = true,
    var isSuperAdmin: Boolean = false,
    var isAdmin: Boolean = false,
    var isResidence: Boolean = true,
)
