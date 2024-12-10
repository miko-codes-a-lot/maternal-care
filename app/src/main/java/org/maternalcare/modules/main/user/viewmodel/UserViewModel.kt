package org.maternalcare.modules.main.user.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.maternalcare.modules.main.user.model.dto.UserBirthRecordDto
import org.maternalcare.modules.main.user.model.dto.UserCheckupDto
import org.maternalcare.modules.main.user.model.dto.UserConditionDto
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.model.dto.UserImmunizationDto
import org.maternalcare.modules.main.user.model.dto.UserTrimesterRecordDto
import org.maternalcare.modules.main.user.service.UserReport
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

    fun fetchUserCheckupByNumber(userId: String, checkUpNumber: Int, pregnantRecordId: String, pregnantTrimesterId: String): UserCheckupDto? {
        return userService.fetchCheckupDetailByNumber(userId, checkUpNumber, pregnantRecordId, pregnantTrimesterId)
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

    fun fetchUserConditionRecord(userId: String): UserConditionDto? {
        return userService.fetchUserConditionByRecord(userId)
    }

    suspend fun upsertCondition(conditionDto: UserConditionDto): Result<UserConditionDto> {
        return this.userService.upsertCondition(conditionDto)
    }

    fun fetchUserImmunization(userId: String, pregnantRecordId: String): UserImmunizationDto? {
        return userService.fetchUserImmunizationByUserId(userId, pregnantRecordId)
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

    fun fetchPregnancyUser(pregnancyRecordId: String): UserBirthRecordDto {
        return userService.fetchOnePregnancy(pregnancyRecordId)
    }

    fun getHealthRecords(userId: String): List<UserBirthRecordDto> {
        return userService.fetchListHealthRecordUser(userId)
    }

    suspend fun upsertHealthRecord(healthRecordDto: UserBirthRecordDto): Result<UserBirthRecordDto> {
        return this.userService.upsertHealthRecord(healthRecordDto)
    }

    fun fetchTrimester(trimesterUserId: String): UserTrimesterRecordDto {
        return userService.fetchOneTrimester(trimesterUserId)
    }

    fun getTrimesterRecords(pregnantTrimesterId: String, pregnantRecordId: String): List<UserTrimesterRecordDto> {
        return userService.fetchListTrimesterRecordUser(pregnantTrimesterId, pregnantRecordId)
    }

    suspend fun upsertTrimesterRecord(trimesterRecordDto: UserTrimesterRecordDto): Result<UserTrimesterRecordDto> {
        return this.userService.upsertTrimesterRecord(trimesterRecordDto)
    }

    fun fetchResidencesReportDetails(addressName: String): List<UserReport>{
        return userService.fetchResidencesWithDetails(addressName);
    }
}