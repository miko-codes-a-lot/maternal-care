package org.maternalcare.modules.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.maternalcare.modules.main.menu.MenuUI
import org.maternalcare.modules.main.residence.residenceGraph

fun NavGraphBuilder.mainGraph(navController: NavController) {
    navigation<MainNav>(startDestination = MainNav.Menu) {
        composable<MainNav.Menu> {
            MenuUI(navController)
        }
        residenceGraph(navController)
    }
}