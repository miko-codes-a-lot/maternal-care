package org.maternalcare.modules.main.residence.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.user.model.dto.UserBirthRecordDto
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.model.dto.UserTrimesterRecordDto

@Composable
fun TrimesterCheckUpListUI(
    navController: NavController,
    userDto: UserDto,
    pregnantRecordId: UserBirthRecordDto,
    pregnantTrimesterId: UserTrimesterRecordDto
) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Trimester No. ${pregnantTrimesterId.trimesterOrder}",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            fontFamily = FontFamily.SansSerif,
            color = Color(0xFF6650a4)
        )
        Spacer(modifier = Modifier.height(20.dp))
        CheckUpNavigationButton(navController, userDto, pregnantRecordId, pregnantTrimesterId)
    }
}

data class SelectionCheckUp(val text: String, val action: () -> Unit)

@Composable
fun CheckUpNavigationButton(
    navController: NavController,
    userDto: UserDto,
    pregnantRecordId: UserBirthRecordDto,
    pregnantTrimesterId: UserTrimesterRecordDto
) {
    val listOfCheckup = listOf(
        SelectionCheckUp(text = "Checkup 1") {
            navController.navigate(MainNav.CheckupDetails(userId = userDto.id!!, checkupNumber = 1, pregnantRecordId = pregnantRecordId.id!!, pregnantTrimesterId = pregnantTrimesterId.id!!))
        },
        SelectionCheckUp(text = "Checkup 2") {
            navController.navigate(MainNav.CheckupDetails(userId = userDto.id!!, checkupNumber = 2, pregnantRecordId = pregnantRecordId.id!!, pregnantTrimesterId = pregnantTrimesterId.id!!))
        },
        SelectionCheckUp(text = "Checkup 3") {
            navController.navigate(MainNav.CheckupDetails(userId = userDto.id!!, checkupNumber = 3, pregnantRecordId = pregnantRecordId.id!!, pregnantTrimesterId = pregnantTrimesterId.id!!))
        }
    )
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center
    ) {
        listOfCheckup.forEach { checkup ->
            Spacer(modifier = Modifier.height(15.dp))
            ButtonContainer(onClick = checkup.action, text = checkup.text)
        }
    }
}