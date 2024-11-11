package org.maternalcare.modules.main.user.model.mapper

import org.maternalcare.modules.main.user.model.dto.AddressDto
import org.maternalcare.modules.main.user.model.entity.Address
import org.maternalcare.shared.ext.toObjectId

fun Address.toDto(): AddressDto {
    return AddressDto(
        id = _id.toHexString(),
        name = name,
        code = code,
        latitude = latitude,
        longitude = longitude
    )
}

fun AddressDto.toEntity(): Address {
    val userDto = this
    return Address().apply {
        _id = userDto.id.toObjectId()
        name = userDto.name
        code = userDto.code
        latitude = userDto.latitude
        longitude = userDto.longitude
    }
}