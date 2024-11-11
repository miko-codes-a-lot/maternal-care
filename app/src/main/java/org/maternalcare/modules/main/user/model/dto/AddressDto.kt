package org.maternalcare.modules.main.user.model.dto

data class AddressDto(
    var id: String?,
    var name: String,
    var code: String,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
)