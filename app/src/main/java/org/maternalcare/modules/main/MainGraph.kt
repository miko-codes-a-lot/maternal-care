package org.maternalcare.modules.main

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
import org.maternalcare.modules.main.residence.ui.AddressesUI
import org.maternalcare.modules.main.residence.ui.CheckupDetailsUI
import org.maternalcare.modules.main.residence.ui.ChooseCheckupUI
import org.maternalcare.modules.main.residence.ui.EditCheckupUI
import org.maternalcare.modules.main.residence.ui.ResidencesPreviewUI
import org.maternalcare.modules.main.residence.ui.ResidencesUI
import org.maternalcare.modules.main.residence.viewmodel.ResidenceViewModel
import org.maternalcare.modules.main.settings.ui.EditSettingsUI
import org.maternalcare.modules.main.settings.ui.SettingsUI
import org.maternalcare.modules.main.user.model.dto.AddressDto
import org.maternalcare.modules.main.user.model.dto.UserCheckupDto
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
                ResidencesUI(
                    navController = navController,
                    currentUser = currentUser,
                    addressDto = addressDto,
                    isArchive = args.isArchive,
                    dateOfCheckup = args.dateOfCheckUp,
                )
            }
        }
        composable<MainNav.ResidencePreview> {
            val args = it.toRoute<MainNav.ChooseCheckup>()
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
                ChooseCheckupUI(navController, currentUser, userDto)
            }
        }
        composable<MainNav.CheckupDetails> {
            val args = it.toRoute<MainNav.CheckupDetails>()
            val userViewModel: UserViewModel = hiltViewModel()
            Guard(navController = navController) { currentUser ->
                val checkupDto = userViewModel.fetchUserCheckupByNumber(args.userId, args.checkupNumber)
                val userDto = userViewModel.fetchUser(userId = args.userId)
                if (checkupDto != null || currentUser.isSuperAdmin) {
                    CheckupDetailsUI(
                        navController,
                        currentUser,
                        checkupDto = checkupDto ?: UserCheckupDto(),
                        userDto,
                        checkupNumber = args.checkupNumber
                    )
                } else {
                    EditCheckupUI(
                        navController,
                        checkupNumber = args.checkupNumber,
                        userDto = userDto,
                        currentUser = currentUser,
                        checkupUser = checkupDto
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
                EditCheckupUI(
                    navController,
                    checkupNumber = args.checkupNumber,
                    userDto = userDto,
                    checkupUser = checkup,
                    currentUser = currentUser
                )
            }
        }
        composable<MainNav.Reminders> {
            Guard(navController = navController) { currentUser ->
                if(currentUser.isAdmin){
                    ReminderListUI(navController, currentUser)
                }else{
//                    ReminderDates(onDismiss = { /*TODO*/ }, currentUser = , checkupDto = )
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
                    userViewModel = userViewModel
                )
            }
        }
        composable<MainNav.UserPreview> {
            val userViewModel: UserViewModel = hiltViewModel()
//            UserPreviewUI(navController = navController, user = UserDto(), title = "Preview Account", onSave = { userDto ->
//                userViewModel.viewModelScope.launch {
//                    val result = userViewModel.upsertUser(userDto)
//                    if (result.isSuccess) { navController.popBackStack() }
//                }
//            })
        }
    }
}