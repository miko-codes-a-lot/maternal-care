package org.maternalcare.modules.main.menu.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import org.maternalcare.R
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.menu.model.MenuItem
import org.maternalcare.modules.main.residence.enum.CheckupStatus
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.viewmodel.UserViewModel
import org.maternalcare.shared.ui.ReminderCheckupListUI
import org.maternalcare.shared.ui.ReminderDates

@Composable
fun MenuUI(
    navController: NavController,
    currentUser: UserDto,
) {
    val isReminderAlertVisible = rememberSaveable { mutableStateOf(!currentUser.isSuperAdmin) }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.care),
                contentDescription = "Login Image",
                modifier = Modifier
                    .size(100.dp)
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally)
            )
            if (isReminderAlertVisible.value) {
                val userViewModel: UserViewModel = hiltViewModel()
                if (currentUser.isResidence) {
                    val checkupDto = userViewModel.fetchCheckUpDetail(currentUser.id!!)
                    if (checkupDto != null) {
                        ReminderDates(
                            isReminderAlert = true,
                            onDismiss = { isReminderAlertVisible.value = false },
                            currentUser = currentUser,
                            checkupDto = checkupDto,
                        )
                    }
                } else if (currentUser.isAdmin) {
                    val checkupDates = userViewModel.getGroupOfCheckupDate(currentUser.id!!)
                    ReminderCheckupListUI(
                        onDismiss = {  isReminderAlertVisible.value = false  },
                        checkupDto = checkupDates
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            UserPosition(userDto = currentUser)
            Spacer(modifier = Modifier.height(13.dp))
            Menu(navController = navController, userDto = currentUser)
        }
    }
}

@Composable
fun UserPosition(userDto: UserDto) {
    val userDetails = when {
        userDto.isSuperAdmin -> listOf("MidWife")
        userDto.isAdmin -> listOf("BHW")
        userDto.isResidence -> listOf("Pregnant")
        else -> listOf("Unknown")
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        userDetails.forEach { userPosition ->
            Text(
                text = userPosition,
                fontSize = 26.sp,
                fontFamily = FontFamily.SansSerif
            )
        }
    }
}

@Composable
private fun Menu(navController: NavController, userDto: UserDto) {
    val menuItems = getMenuItems(navController = navController, userDto = userDto)
    Box(
        modifier = Modifier
            .border(
                BorderStroke(2.dp, Color(0xFF6650a4)),
                RoundedCornerShape(14.dp),
            )
            .background(
                Color(0xFFf3f0fc),
                shape = RoundedCornerShape(14.dp)
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(menuItems) { menuItem ->
                Spacer(modifier = Modifier.padding(top = 4.dp))

                MenuButton(
                    text = menuItem.text,
                    onClick = menuItem.action,
                    iconResId = menuItem.iconResId
                )
                Spacer(modifier = Modifier.padding(bottom = 10.dp))
            }
        }
    }
}

fun getMenuItems(userDto: UserDto, navController: NavController): List<MenuItem> {
    return when {
        userDto.isSuperAdmin -> listOf(
            MenuItem(
                text = "Pregnant Users",
                action = { navController.navigate(MainNav.Addresses(CheckupStatus.ALL.name)) },
                iconResId = R.drawable.pregnant
            ),
            MenuItem(
                text = "Dashboard",
                action = { navController.navigate(MainNav.Dashboard) },
                iconResId = R.drawable.chart
            ),
            MenuItem(
                text = "Manage User",
                action = { navController.navigate(MainNav.User) },
                iconResId = R.drawable.manage_user
            ),
            MenuItem(
                text = "Backup Storage",
                action = {  navController.navigate(MainNav.Addresses(CheckupStatus.ALL.name, isArchive = true)) },
                iconResId = R.drawable.backup
            ),
            MenuItem(
                text = "Settings",
                action = { navController.navigate(MainNav.Settings) },
                iconResId = R.drawable.settings
            )
        )
        userDto.isAdmin -> listOf(
            MenuItem(
                text = "Pregnant Users",
                action = { navController.navigate(MainNav.Addresses(CheckupStatus.ALL.name)) },
                iconResId = R.drawable.pregnant
            ),
            MenuItem(
                text = "Messages",
                action = { navController.navigate(MainNav.ChatLobby) },
                iconResId = R.drawable.message
            ),
            MenuItem(
                text = "Reminders",
                action = { navController.navigate(MainNav.ReminderLists) },
                iconResId = R.drawable.reminder
            ),
            MenuItem(
                text = "Backup Storage",
                action = {  navController.navigate(MainNav.Addresses(CheckupStatus.ALL.name, isArchive = true)) },
                iconResId = R.drawable.backup
            ),
            MenuItem(
                text = "Settings",
                action = { navController.navigate(MainNav.Settings) },
                iconResId = R.drawable.settings
            )
        )
        userDto.isResidence -> listOf(
            MenuItem(
                text = "Profile",
                action = { navController.navigate(MainNav.HealthRecord(userId = userDto.id!!)) },
                iconResId = R.drawable.pregnant
            ),
            MenuItem(
                text = "Messages",
                action = { navController.navigate(MainNav.ChatDirect(userId = userDto.createdById!!)) },
                iconResId = R.drawable.message
            ),
            MenuItem(
                text = "Reminders",
                action = { navController.navigate(MainNav.ReminderLists) },
                iconResId = R.drawable.reminder
            ),
            MenuItem(
                text = "Settings",
                action = { navController.navigate(MainNav.Settings) },
                iconResId = R.drawable.settings
            )
        )
        else -> listOf()
    }
}

@Composable
private fun MenuButton(text: String, onClick: () -> Unit, iconResId: Int) {
    ElevatedButton(onClick = onClick,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor =  Color(0xFF6650a4),
            contentColor = Color(0xFFFFFFFF)
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
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = "$text Icon",
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = text,
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier
                    .padding(end = 18.dp)
                    .weight(1f)
            )
        }
    }
}

@Composable
fun TextContainer(text: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (text.contains("Reminder")) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                color = Color(0xFF6650a4),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = (FontFamily.SansSerif),
                modifier = Modifier.padding(bottom = 10.dp)
            )
        } else {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color(0xFF6650a4),
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp),
                fontFamily = (FontFamily.SansSerif)
            )
        }
    }
}