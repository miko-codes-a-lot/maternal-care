package org.maternalcare.modules.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.maternalcare.modules.main.dashboard.ui.DashboardUI
import org.maternalcare.modules.main.menu.MenuUI
import org.maternalcare.modules.main.residence.ui.AddressesUI
import org.maternalcare.modules.main.residence.ui.CheckupDetailsUI
import org.maternalcare.modules.main.residence.ui.ChooseCheckupUI
import org.maternalcare.modules.main.residence.ui.ResidencesUI

fun NavGraphBuilder.mainGraph(navController: NavController) {
    navigation<MainNav>(startDestination = MainNav.Menu) {
        composable<MainNav.Menu> {
            MenuUI(navController)
        }
        composable<MainNav.Addresses> {
            AddressesUI(navController)
        }
        composable<MainNav.Residences> {
            ResidencesUI(navController)
        }
        composable<MainNav.ChooseCheckup> {
            ChooseCheckupUI(navController)
        }
        composable<MainNav.CheckupDetails> {
            CheckupDetailsUI(navController)
        }
        composable<MainNav.Dashboard> {
            DashboardUI(navController)
        }
    }
}