package org.maternalcare.modules.main.residence.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.maternalcare.modules.main.user.model.dto.AddressDto
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.service.AddressService
import org.maternalcare.modules.main.user.service.UserService
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

@HiltViewModel
class ResidenceViewModel @Inject constructor(
    private val userService: UserService,
    private val addressService: AddressService
) : ViewModel() {
    fun fetchAddresses(): List<AddressDto> {
        return addressService.fetch()
    }

    fun fetchOneAddress(addressId: ObjectId): AddressDto {
        return addressService.fetchOne(addressId)
    }

    suspend fun upsertAddress(addressDto: AddressDto): Result<AddressDto> {
        return addressService.upsert(addressDto)
    }

    fun fetchUsers(userId: ObjectId, isSuperAdmin: Boolean = false, addressName: String): List<UserDto> {
        val id = if (!isSuperAdmin) userId else null
        return userService.fetch(isResidence = true, userId = id, addressName = addressName)
    }
}