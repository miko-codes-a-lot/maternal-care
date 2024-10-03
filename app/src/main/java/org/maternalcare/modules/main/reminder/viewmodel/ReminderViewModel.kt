package org.maternalcare.modules.main.reminder.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.maternalcare.modules.main.user.model.dto.UserCheckupDto
import org.maternalcare.modules.main.user.service.UserService
import org.maternalcare.shared.ext.toObjectId
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val userService: UserService,
) : ViewModel() {
    fun getGroupOfCheckupDate(adminId: String): List<UserCheckupDto> {
        return userService.getGroupOfCheckupDates(adminId = adminId.toObjectId())
    }

    fun getMyUpcomingCheckup(residenceId: String): List<UserCheckupDto> {
        return userService.getMyUpcomingCheckup(residenceId = residenceId)
    }
}