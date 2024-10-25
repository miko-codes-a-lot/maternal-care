package org.maternalcare.modules.main.user.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.maternalcare.modules.main.user.model.dto.UserCheckupDto
import org.maternalcare.modules.main.user.model.dto.UserConditionDto
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.model.dto.UserImmunizationDto
import org.maternalcare.modules.main.user.service.UserService
import org.maternalcare.shared.ext.toObjectId
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    val userService: UserService
): ViewModel() {
    init {
        viewModelScope.launch {
            userService.archiveOldResidences()
            userService.updateCompletionStatus()
        }
    }

    fun fetchUsers(): List<UserDto> {
        return this.userService.fetch()
    }

    suspend fun upsertUser(userDto: UserDto, actionOf: UserDto): Result<UserDto> {
        return this.userService.upsert(userDto, actionOf)
    }

    fun fetchUser(userId: String): UserDto {
        return this.userService.fetchOne(userId)
    }

    fun fetchUserCheckup(checkUpId: String): UserCheckupDto {
        return this.userService.fetchOneCheckup(checkUpId.toObjectId())
    }

    fun fetchUserCheckupByNumber(userId: String, checkUpNumber: Int): UserCheckupDto? {
        return userService.fetchCheckupDetailByNumber(userId, checkUpNumber)
    }

    suspend fun upsertCheckUp(checkupDto: UserCheckupDto): Result<UserCheckupDto> {
        return this.userService.upsertCheckUp(checkupDto)
    }

    fun fetchCheckUpDetail(userId: String): UserCheckupDto? {
        return userService.fetchCheckUpDetails(userId)
    }

    fun getGroupOfCheckupDate(adminId: String): List<UserCheckupDto> {
        return userService.getGroupOfCheckupDates(adminId.toObjectId())
    }

    fun getCompleteCheckupPercentages(isArchive: Boolean = false): Map<String, Double> {
        return userService.fetchAddressCheckupPercentage(isArchive)
    }

    fun getAllListAddressCheckup(isArchive: Boolean = false): Map<String, Map<String, Int>> {
        return userService.fetchAddressWithCompleteCheckup(isArchive)
    }

    fun fetchUserCondition(userId: String): UserConditionDto? {
        return userService.fetchUserConditionByUserId(userId)
    }

    suspend fun upsertCondition(conditionDto: UserConditionDto): Result<UserConditionDto> {
        return this.userService.upsertCondition(conditionDto)
    }

    fun fetchUserImmunization(userId: String): UserImmunizationDto? {
        return userService.fetchUserImmunizationByUserId(userId)
    }

    suspend fun upsertImmunization(immunizationDto: UserImmunizationDto): Result<UserImmunizationDto> {
        return this.userService.upsertImmunization(immunizationDto)
    }

    fun fetchUserByEmail(email: String): UserDto? {
        return userService.fetchByEmail(email)
    }

    fun fetchUserByEmailAndToken(email: String, token: String): UserDto? {
        return userService.fetchEmailAndToken(email, token)
    }

}