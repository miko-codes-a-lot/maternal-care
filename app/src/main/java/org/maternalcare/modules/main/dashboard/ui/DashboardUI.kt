package org.maternalcare.modules.main.dashboard.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.maternalcare.R
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.residence.enum.CheckupStatus
import org.maternalcare.modules.main.user.model.dto.UserDto

@Composable
fun DashboardUI(navController: NavController, userDto: UserDto, isArchive: Boolean = false) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column (
             modifier = Modifier
                 .padding(16.dp)
                 .fillMaxSize(),
             verticalArrangement = Arrangement.Center,
             horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Dashboard",
                fontSize = 24.sp,
                fontFamily = FontFamily.Serif
            )
            Spacer(modifier = Modifier.height(15.dp))
            Box(
                modifier = Modifier
                    .size(400.dp)
            ) {
                ChartUI(
                    userDto = userDto,
                    addressDto = null,
                    isArchive = isArchive
                )
            }
            DashboardMenu(navController)
        }
    }
}

@Composable
private fun DashboardMenu (navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .height(300.dp)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            DashboardButton(text = "  Pregnant Records", iconResId = R.drawable.pregnant_list) {
                navController.navigate(MainNav.Residences(status = CheckupStatus.PREGNANT.name, isArchive = false, isDashboard = true))
            }
        }

        item {
            DashboardButton(text = "Normal List", iconResId = R.drawable.normal_safety) {
                navController.navigate(MainNav.Residences(status = CheckupStatus.NORMAL.name, isArchive = false, isDashboard = true))
            }
        }

        item {
            DashboardButton(text = "Critical List", iconResId = R.drawable.critical_healing) {
                navController.navigate(MainNav.Residences(status = CheckupStatus.CRITICAL.name, isArchive = false, isDashboard = true))
            }
        }

        item {
            DashboardButton(
                text = "Complete", iconResId = R.drawable.check) {
                navController.navigate(MainNav.MonitoringCheckup(isComplete = true, dashboard = true, isArchive = false))
            }
        }

        item {
            DashboardButton(text = "Incomplete", iconResId = R.drawable.clear) {
                navController.navigate(MainNav.MonitoringCheckup(isComplete = false, dashboard = false, isArchive = false))
            }
        }
    }
}

@Composable
private fun DashboardButton(text: String,iconResId: Int? = null, onClick: () -> Unit) {
    ElevatedButton(
        onClick = onClick,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = Color(0xFF6650a4),
              contentColor = Color.White
        ),
        modifier = Modifier
            .width(280.dp)
            .height(63.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            iconResId?.let {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = text,
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                modifier = Modifier
                    .padding(end = 18.dp)
                    .weight(1f)
            )
        }
    }
}
