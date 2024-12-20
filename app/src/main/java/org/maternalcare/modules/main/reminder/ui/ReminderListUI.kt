package org.maternalcare.modules.main.reminder.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.reminder.viewmodel.ReminderViewModel
import org.maternalcare.modules.main.residence.enum.CheckupStatus
import org.maternalcare.modules.main.user.model.dto.UserCheckupDto
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.shared.ui.formatListDates

@Preview(showSystemUi = true)
@Composable
fun ReminderListUIPreview() {
    ReminderListUI(
        navController = rememberNavController(),
        currentUser = UserDto(isAdmin = true)
    )
}

@Composable
fun ReminderListUI(
    navController: NavController,
    currentUser: UserDto
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color.White)
            .fillMaxWidth(),
         horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Prenatal Check-Up Scheduled",
                fontFamily = FontFamily.SansSerif,
                fontSize = 21.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth(),
                color = Color(0xFF6650a4)
            )
            Spacer(modifier = Modifier.height(15.dp))
            ScheduleList(navController, currentUser)
        }
    }
}

@Composable
private fun RemindersButton (data: UserCheckupDto, onClick: () -> Unit, navController: NavController){
    ElevatedButton(onClick = onClick,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor =  Color(0xFF6650a4),
            contentColor = Color(0xFFFFFFFF)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp)
            .height(60.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Text(
            text = formatListDates(data.scheduleOfNextCheckUp),
            fontSize = 17.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
        )
    }
}

@Composable
fun ScheduleList (navController: NavController, currentUser: UserDto) {
    val reminderViewModel: ReminderViewModel = hiltViewModel()
    val reminders =
        if (currentUser.isAdmin)
            reminderViewModel.getGroupOfCheckupDate(currentUser.id!!)
        else
            reminderViewModel.getMyUpcomingCheckup(currentUser.id!!)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(reminders) { reminder ->
            RemindersButton(data = reminder, navController = navController, onClick = {
                if (!currentUser.isAdmin) return@RemindersButton

                val route = MainNav.Residences(
                    status = CheckupStatus.ALL.name,
                    dateOfCheckUp = reminder.scheduleOfNextCheckUp,
                    isDashboard = false
                )
                navController.navigate(route)
            })
        }
    }
}
