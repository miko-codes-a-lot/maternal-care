package org.maternalcare.modules.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import org.maternalcare.modules.main.dashboard.ui.CheckupProgressUI
import org.maternalcare.modules.main.dashboard.ui.DashboardUI
import org.maternalcare.modules.main.menu.ui.MenuUI
import org.maternalcare.modules.main.message.ui.MessageListUI
import org.maternalcare.modules.main.message.ui.MessageUI
import org.maternalcare.modules.main.reminder.ui.ReminderListUI
import org.maternalcare.modules.main.residence.enum.CheckupStatus
import org.maternalcare.modules.main.residence.ui.AddressesUI
import org.maternalcare.modules.main.residence.ui.CheckupDetailsUI
import org.maternalcare.modules.main.residence.ui.ChooseCheckupUI
import org.maternalcare.modules.main.residence.ui.ConditionStatusUI
import org.maternalcare.modules.main.residence.ui.CreateHealthRecordUI
import org.maternalcare.modules.main.residence.ui.EditCheckupUI
import org.maternalcare.modules.main.residence.ui.HealthRecordListUI
import org.maternalcare.modules.main.residence.ui.ImmunizationRecordUI
import org.maternalcare.modules.main.residence.ui.ResidencesPreviewUI
import org.maternalcare.modules.main.residence.ui.ResidencesUI
import org.maternalcare.modules.main.residence.ui.StatusPreviewUI
import org.maternalcare.modules.main.residence.ui.TrimesterCheckUpListUI
import org.maternalcare.modules.main.residence.ui.CreateTrimesterRecordUI
import org.maternalcare.modules.main.residence.viewmodel.ResidenceViewModel
import org.maternalcare.modules.main.settings.ui.EditSettingsUI
import org.maternalcare.modules.main.settings.ui.SettingsUI
import org.maternalcare.modules.main.user.model.dto.AddressDto
import org.maternalcare.modules.main.user.model.dto.UserBirthRecordDto
import org.maternalcare.modules.main.user.model.dto.UserCheckupDto
import org.maternalcare.modules.main.user.model.dto.UserConditionDto
import org.maternalcare.modules.main.user.model.dto.UserImmunizationDto
import org.maternalcare.modules.main.user.model.dto.UserTrimesterRecordDto
import org.maternalcare.modules.main.user.service.UserService
import org.maternalcare.modules.main.user.ui.UserCreateUI
import org.maternalcare.modules.main.user.ui.UserEditUI
import org.maternalcare.modules.main.user.ui.UsersUI
import org.maternalcare.modules.main.user.viewmodel.UserViewModel
import org.maternalcare.shared.Guard
import org.maternalcare.shared.ext.toObjectId

fun NavGraphBuilder.mainGraph(navController: NavController) {
    navigation<MainNav>(startDestination = MainNav.Menu) {
        composable<MainNav.Menu> {
            Guard(navController = navController) { currentUser ->
                MenuUI(navController, currentUser)
            }
        }
        composable<MainNav.Addresses> {
            val args = it.toRoute<MainNav.Addresses>()
            Guard(navController = navController) { currentUser ->
                AddressesUI(navController, isArchive = args.isArchive)
            }
        }
        composable<MainNav.Residences> {
            val args = it.toRoute<MainNav.Residences>()
            val residenceViewModel: ResidenceViewModel = hiltViewModel()
            var addressDto: AddressDto? = null;
            if (args.addressId != null) {
                addressDto = residenceViewModel.fetchOneAddress(args.addressId.toObjectId())
            }
            Guard(navController = navController) { currentUser ->
                val isCompleted = when (args.status) {
                    CheckupStatus.COMPLETE.name -> true
                    CheckupStatus.INCOMPLETE.name -> false
                    else -> null
                }
                ResidencesUI(
                    navController = navController,
                    currentUser = currentUser,
                    addressDto = addressDto,
                    isArchive = args.isArchive,
                    isCompleted = isCompleted,
                    dateOfCheckup = args.dateOfCheckUp,
                    isDashboard = args.isDashboard,
                    status = args.status
                )
            }
        }
        composable<MainNav.ResidencePreview> {
            val args = it.toRoute<MainNav.ResidencePreview>()
            val userViewModel: UserViewModel = hiltViewModel()
            Guard(navController = navController) { currentUser ->
                val userDto = userViewModel.fetchUser(args.userId)
                ResidencesPreviewUI(navController = navController, currentUser, userDto)
            }
        }
        composable<MainNav.ChooseCheckup> {
            val args = it.toRoute<MainNav.ChooseCheckup>()
            val userViewModel: UserViewModel = hiltViewModel()

            Guard(navController = navController) { currentUser ->
                val userDto = userViewModel.fetchUser(userId = args.userId)
                val conditionStatus = userViewModel.fetchUserCondition(args.userId)
                val pregnantRecordId = userViewModel.fetchPregnancyUser(args.pregnantRecordId)
                val trimesterRecord = userViewModel.getTrimesterRecords(
                    pregnantTrimesterId = args.userId,
                    pregnantRecordId = args.pregnantRecordId
                )
                ChooseCheckupUI(
                    navController = navController,
                    currentUser = currentUser,
                    userDto = userDto,
                    conditionStatus = conditionStatus,
                    pregnantRecordId = pregnantRecordId,
                    trimesterRecord = trimesterRecord,
                )
            }
        }
        composable<MainNav.CheckupDetails> {
            val args = it.toRoute<MainNav.CheckupDetails>()
            val userViewModel: UserViewModel = hiltViewModel()
            Guard(navController = navController) { currentUser ->
                val checkupDto = userViewModel.fetchUserCheckupByNumber(args.userId, args.checkupNumber, args.pregnantRecordId, args.pregnantTrimesterId)
                val userDto = userViewModel.fetchUser(userId = args.userId)
                val pregnantRecordId = userViewModel.fetchPregnancyUser(args.pregnantRecordId)
                val trimesterRecordId = userViewModel.fetchTrimester(args.pregnantTrimesterId)
                if (checkupDto != null || currentUser.isSuperAdmin || currentUser.isResidence) {
                    CheckupDetailsUI(
                        navController,
                        currentUser,
                        checkupDto = checkupDto ?: UserCheckupDto(),
                        userDto,
                        checkupNumber = args.checkupNumber,
                        pregnantRecordId = pregnantRecordId,
                        trimesterRecordId = trimesterRecordId
                    )
                } else {
                    EditCheckupUI(
                        navController,
                        checkupNumber = args.checkupNumber,
                        userDto = userDto,
                        currentUser = currentUser,
                        checkupUser = checkupDto,
                        pregnantRecordId = pregnantRecordId,
                        trimesterRecordId = trimesterRecordId
                    )
                }
            }
        }
        composable<MainNav.CheckupDetailsEdit> {
            val args = it.toRoute<MainNav.CheckupDetailsEdit>()
            val userViewModel: UserViewModel = hiltViewModel()

            Guard(navController) { currentUser ->
                val checkup = userViewModel.fetchUserCheckup(args.checkupId)
                val userDto = userViewModel.fetchUser(args.userId)
                val pregnancyDto = userViewModel.fetchPregnancyUser(args.pregnantRecordId)
                val trimesterDto = userViewModel.fetchTrimester(args.pregnantTrimesterId)
                EditCheckupUI(
                    navController,
                    checkupNumber = args.checkupNumber,
                    userDto = userDto,
                    checkupUser = checkup,
                    currentUser = currentUser,
                    pregnantRecordId = pregnancyDto,
                    trimesterRecordId = trimesterDto
                )
            }
        }
        composable<MainNav.ConditionStatus> {
            val args = it.toRoute<MainNav.ConditionStatus>()
            val userViewModel: UserViewModel = hiltViewModel()
            val userConditionDto by remember { mutableStateOf<UserConditionDto?>(null) }
            Guard(navController = navController) { currentUser ->
                if(userConditionDto != null){
                    val userDto = userViewModel.fetchUser(args.userId)
                    ConditionStatusUI(
                        navController = navController,
                        userDto = userDto,
                        currentUser = currentUser,
                        userCondition = userConditionDto
                    )
                }else{
                    val userDto = userViewModel.fetchUser(args.userId)
                    val conditionDto = userViewModel.fetchUserCondition(args.userId)
                    ConditionStatusUI(
                        navController = navController,
                        userDto = userDto,
                        currentUser = currentUser,
                        userCondition = conditionDto
                    )
                }
            }
        }
        composable<MainNav.StatusPreview> {
            val args = it.toRoute<MainNav.StatusPreview>()
            val userViewModel: UserViewModel = hiltViewModel()
            val conditionDto = userViewModel.fetchUserCondition(args.userId)
            Guard(navController = navController) { currentUser ->
                if (conditionDto != null) {
                    StatusPreviewUI(
                        userCondition = conditionDto,
                    )
                }
            }
        }
        composable<MainNav.ImmunizationRecord> {
            val args = it.toRoute<MainNav.ImmunizationRecord>()
            val userViewModel: UserViewModel = hiltViewModel()
            val immunizationDto = userViewModel.fetchUserImmunization(args.userId, args.pregnantRecordId)
            val pregnancyDto = userViewModel.fetchPregnancyUser(args.pregnantRecordId)
            Guard(navController = navController) { currentUser ->
                if(immunizationDto != null){
                    val userDto = userViewModel.fetchUser(args.userId)
                    ImmunizationRecordUI(
                        navController = navController,
                        userDto = userDto,
                        userImmunization = immunizationDto,
                        currentUser = currentUser,
                        pregnantRecord = pregnancyDto,
                    )
                }
                else{
                    val userDto = userViewModel.fetchUser(args.userId)
                    ImmunizationRecordUI(
                        navController = navController,
                        userDto = userDto,
                        userImmunization = UserImmunizationDto(),
                        currentUser = currentUser,
                        pregnantRecord = pregnancyDto,
                    )
                }
            }
        }
        composable<MainNav.Reminders> {
            Guard(navController = navController) { currentUser ->
                if(currentUser.isAdmin){
                    ReminderListUI(navController, currentUser)
                }
            }
        }
        composable<MainNav.MessagesList> {
            Guard(navController = navController) { currentUser ->
                MessageListUI(navController, currentUser)
            }
        }
        composable<MainNav.Messages> {
            val args = it.toRoute<MainNav.Messages>()

            val userViewModel: UserViewModel = hiltViewModel()
            val userDto = userViewModel.fetchUser(args.userId)

            Guard(navController = navController) { currentUser ->
                MessageUI(navController, currentUser = currentUser,  userDto = userDto)
            }
        }
        composable<MainNav.ReminderLists> {
            Guard(navController = navController) { currentUser ->
                ReminderListUI(navController, currentUser)
            }
        }
        composable<MainNav.Dashboard> {
            Guard(navController = navController) { currentUser ->
                DashboardUI(navController)
            }
        }
        composable<MainNav.User> {
            Guard(navController = navController) { currentUser ->
                UsersUI(navController)
            }
        }
        composable<MainNav.CreateUser> {
            val args = it.toRoute<MainNav.CreateUser>()
            val residenceViewModel: ResidenceViewModel = hiltViewModel()
            val addressDto =
                if(args.addressId != null)
                    residenceViewModel.fetchOneAddress(args.addressId.toObjectId())
                else
                    null

            Guard(navController = navController) { currentUser ->
                UserCreateUI(navController, currentUser, addressDto)
            }
        }
        composable<MainNav.EditUser> {
            val args = it.toRoute<MainNav.EditUser>()
            val userViewModel: UserViewModel = hiltViewModel()
            val userDto = userViewModel.fetchUser(args.userId)
            Guard(navController = navController) { currentUser ->
                UserEditUI(navController = navController, currentUser = currentUser, userDto = userDto)
            }
        }
        composable<MainNav.Settings> {
            Guard(navController = navController) { currentUser ->
                val userService: UserService = hiltViewModel<UserViewModel>().userService
                SettingsUI(navController = navController, currentUser = currentUser, userService = userService)
            }
        }
        composable("${MainNav.EditSettings}/{settingType}") { backStackEntry ->
            val settingType = backStackEntry.arguments?.getString("settingType") ?: ""
            Guard(navController = navController) { currentUser ->
                EditSettingsUI(navController, settingType, currentUser)
            }
        }
        composable<MainNav.MonitoringCheckup> {
            val args = it.toRoute<MainNav.MonitoringCheckup>()
            val userViewModel: UserViewModel = hiltViewModel()
            Guard(navController = navController) { currentUser ->
                CheckupProgressUI(
                    navController,
                    isComplete = args.isComplete,
                    isArchive = args.isArchive,
                    userViewModel = userViewModel
                )
            }
        }
        composable<MainNav.HealthRecord> {
            val args = it.toRoute<MainNav.HealthRecord>()
            val userViewModel: UserViewModel = hiltViewModel()
            Guard(navController = navController) { currentUser ->
                val userDto = userViewModel.fetchUser(userId = args.userId)
                val conditionStatus = userViewModel.fetchUserConditionRecord(args.userId)
                val healthRecords = userViewModel.getHealthRecords(args.userId)
                HealthRecordListUI(
                    navController = navController,
                    userDto = userDto,
                    currentUser = currentUser,
                    conditionStatus = conditionStatus,
                    healthRecords = healthRecords
                )
            }
        }
        composable<MainNav.CreateRecord> {
            val args = it.toRoute<MainNav.CreateRecord>()
            val userViewModel: UserViewModel = hiltViewModel()
            Guard(navController = navController) { currentUser ->
                val userDto = userViewModel.fetchUser(args.userId)
                CreateHealthRecordUI(
                    userDto = userDto,
                    navController = navController,
                    pregnantRecord = UserBirthRecordDto()
                )
            }
        }
        composable<MainNav.CreateTrimester> {
            val args = it.toRoute<MainNav.CreateTrimester>()
            val userViewModel: UserViewModel = hiltViewModel()
            Guard(navController = navController) { currentUser ->
                val userDto = userViewModel.fetchUser(args.userId)
                val pregnantRecordDto = userViewModel.fetchPregnancyUser(args.pregnantRecordId)
                val pregnantTrimesterRecords = userViewModel.getTrimesterRecords(
                    pregnantTrimesterId = args.userId,
                    pregnantRecordId = args.pregnantRecordId
                )

                CreateTrimesterRecordUI(
                    userDto = userDto,
                    trimesterRecord = UserTrimesterRecordDto(),
                    navController = navController,
                    pregnantRecordDto = pregnantRecordDto,
                    pregnantTrimesterRecords = pregnantTrimesterRecords
                )
            }
        }
        composable<MainNav.TrimesterCheckUpList> {
            val args = it.toRoute<MainNav.TrimesterCheckUpList>()
            val userViewModel: UserViewModel = hiltViewModel()
            val userDto = userViewModel.fetchUser(userId = args.userId)
            val pregnantRecordId = userViewModel.fetchPregnancyUser(args.pregnantRecordId)
            val pregnantTrimesterId = userViewModel.fetchTrimester(args.trimesterId)
            Guard(navController = navController) { currentUser ->
                TrimesterCheckUpListUI(
                    navController,
                    userDto = userDto,
                    pregnantRecordId = pregnantRecordId,
                    pregnantTrimesterId = pregnantTrimesterId
                )
            }
        }
    }
}