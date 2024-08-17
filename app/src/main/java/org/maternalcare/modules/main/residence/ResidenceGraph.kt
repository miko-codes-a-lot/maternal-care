package org.maternalcare.modules.main.residence

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.residence.ui.AddressesUI
import org.maternalcare.modules.main.residence.ui.CheckupDetailsUI
import org.maternalcare.modules.main.residence.ui.ChooseCheckupUI
import org.maternalcare.modules.main.residence.ui.ResidencesUI

fun NavGraphBuilder.residenceGraph(navController: NavController) {
    navigation<MainNav.Residence>(startDestination = MainNav.Residence.Addresses) {
        composable<MainNav.Residence.Addresses> {
            AddressesUI(navController)
        }
        composable<MainNav.Residence.Residences> {
            ResidencesUI(navController)
        }
        composable<MainNav.Residence.ChooseCheckup> {
            ChooseCheckupUI(navController)
        }
        composable<MainNav.Residence.CheckupDetails> {
            CheckupDetailsUI(navController)
        }
    }
}