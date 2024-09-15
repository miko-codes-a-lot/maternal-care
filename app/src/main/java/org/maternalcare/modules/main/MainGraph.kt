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
import org.maternalcare.modules.main.message.message.ui.MessageUI
import org.maternalcare.modules.main.reminder.ui.ReminderListUI
import org.maternalcare.modules.main.residence.ui.AddressesUI
import org.maternalcare.modules.main.residence.ui.CheckupDetailsUI
import org.maternalcare.modules.main.residence.ui.ChooseCheckupUI
import org.maternalcare.modules.main.residence.ui.EditCheckupUI
import org.maternalcare.modules.main.residence.ui.ResidencesUI
import org.maternalcare.modules.main.settings.ui.EditSettingsUI
import org.maternalcare.modules.main.settings.ui.SettingsUI
import org.maternalcare.modules.main.user.service.UserService
import org.maternalcare.modules.main.user.ui.UserCreateUI
import org.maternalcare.modules.main.user.ui.UserEditUI
import org.maternalcare.modules.main.user.ui.UsersUI
import org.maternalcare.modules.main.user.viewmodel.UserViewModel
import org.maternalcare.shared.Guard

fun NavGraphBuilder.mainGraph(navController: NavController) {
    navigation<MainNav>(startDestination = MainNav.Menu) {
        composable<MainNav.Menu> {
            Guard(navController = navController) { currentUser ->
                MenuUI(navController, currentUser)
            }
        }
        composable<MainNav.Addresses> {
            val args = it.toRoute<MainNav.Residences>()

            Guard(navController = navController) { currentUser ->
                AddressesUI(navController, isArchive = args.isArchive)
            }
        }
        composable<MainNav.Residences> {
            Guard(navController = navController) { currentUser ->
                ResidencesUI(navController)
            }
        }
        composable<MainNav.ChooseCheckup> {
            Guard(navController = navController) { currentUser ->
                ChooseCheckupUI(navController)
            }
        }
        composable<MainNav.CheckupDetails> {
            Guard(navController = navController) { currentUser ->
                CheckupDetailsUI(navController)
            }
        }
        composable<MainNav.MessagesList> {
            Guard(navController = navController) { currentUser ->
                MessageListUI(navController)
            }
        }
        composable<MainNav.Messages> {
            Guard(navController = navController) { currentUser ->
                MessageUI(navController)
            }
        }
        composable<MainNav.ReminderLists> {
            Guard(navController = navController) { currentUser ->
                ReminderListUI(navController)
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
            Guard(navController = navController) { currentUser ->
                UserCreateUI(navController)
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
            Guard(navController = navController) { currentUser ->
                CheckupProgressUI(navController)
            }
        }
        composable<MainNav.EditCheckup> {
            Guard(navController = navController) { currentUser ->
                EditCheckupUI(navController)
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
