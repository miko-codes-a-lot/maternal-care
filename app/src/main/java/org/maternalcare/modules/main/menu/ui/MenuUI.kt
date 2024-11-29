package org.maternalcare.modules.main.menu.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background

import androidx.compose.ui.unit.dp

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import okhttp3.internal.wait
import org.maternalcare.R
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.chat.ui.decodeBase64ToBitmap
import org.maternalcare.modules.main.menu.model.MenuItem
import org.maternalcare.modules.main.residence.enum.CheckupStatus
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.ui.resizeBitmap
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
        modifier = Modifier
            .fillMaxSize(),
            color = Color(0xFF6650a4)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF6650a4)),
            contentAlignment = Alignment.TopCenter
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp, end = 15.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(!currentUser.isSuperAdmin) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clickable { navController.navigate(MainNav.ReminderLists) }
                            .clip(CircleShape)
                            .background(Color(0xFF6650a4))
                            .border(1.dp, Color(0xFF6650a4), CircleShape),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.reminder),
                            contentDescription = "Default placeholder",
                            modifier = Modifier
                                .size(50.dp),
                            tint = Color.White
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .offset(y = (100).dp)
                    .height(220.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Spacer(modifier = Modifier.height(20.dp))
                    if(currentUser.imageBase64 != null){
                        Box(
                            modifier = Modifier
                                .clickable { navController.navigate(MainNav.Settings) }
                                .size(140.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF6650a4))
                                .border(1.dp, Color(0xFFFFFFFF), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            val resizedBitmap = remember(currentUser.imageBase64) {
                                currentUser.imageBase64?.let { base64 ->
                                    decodeBase64ToBitmap(base64)?.let { bitmap ->
                                        resizeBitmap(bitmap, maxWidth = 140, maxHeight = 140)
                                    }
                                }
                            }

                            if (resizedBitmap != null) {
                                Image(
                                    bitmap = resizedBitmap.asImageBitmap(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(140.dp)
                                        .clip(CircleShape)
                                )
                            }

                        }
                    }else{
                        Image(
                            painter = rememberAsyncImagePainter(model = "https://img.freepik.com/premium-vector/default-avatar-profile-icon-social-media-user-image-gray-avatar-icon-blank-profile-silhouette-vector-illustration_561158-3467.jpg"),
                            contentDescription = "User's avatar",
                            modifier = Modifier
                                .size(140.dp)
                                .clickable { navController.navigate(MainNav.Settings) }
                                .clip(CircleShape)
                                .background(Color(0xFF6650a4)),
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))
                    
                    Text(
                        text = "${currentUser.firstName} ${currentUser.middleName} ${currentUser.lastName}",
                        color = Color.White,
                        fontWeight = FontWeight.W800,
                        fontSize = 24.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }
        }

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

        Column(
            Modifier
                .padding(top = 350.dp)
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(Color.White)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(45.dp))
            Row(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                UserPosition(
                    userDto = currentUser
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
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
        horizontalArrangement = Arrangement.Start
    ) {
        userDetails.forEach { userPosition ->
            Text(
                text = userPosition,
                fontSize = 26.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6650a4)
            )
        }
    }
}

@Composable
private fun Menu(navController: NavController, userDto: UserDto) {
    val menuItems = getMenuItems(navController = navController, userDto = userDto)
    LazyColumn(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(menuItems.chunked(2)) { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                rowItems.forEach { menuItem ->
                    MenuButton(
                        text = menuItem.text,
                        onClick = menuItem.action,
                        iconResId = menuItem.iconResId,
                        modifier = Modifier.weight(1f)
                    )
                }

                if (rowItems.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

fun getMenuItems(userDto: UserDto, navController: NavController): List<MenuItem> {
    return when {
        userDto.isSuperAdmin -> listOf(
            MenuItem(
                text = "",
                action = { navController.navigate(MainNav.Addresses(CheckupStatus.ALL.name)) },
                iconResId = R.drawable.pregnant
            ),
            MenuItem(
                text = "",
                action = { navController.navigate(MainNav.Dashboard) },
                iconResId = R.drawable.chart
            ),
            MenuItem(
                text = "",
                action = { navController.navigate(MainNav.User) },
                iconResId = R.drawable.manage_user
            ),
            MenuItem(
                text = "",
                action = {  navController.navigate(MainNav.Addresses(CheckupStatus.ALL.name, isArchive = true)) },
                iconResId = R.drawable.backup
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
                text = "",
                action = {  navController.navigate(MainNav.Addresses(CheckupStatus.ALL.name, isArchive = true)) },
                iconResId = R.drawable.backup
                ),
            MenuItem(
                text = "",
                action = { navController.navigate(MainNav.Settings) },
                iconResId = R.drawable.settings
            )
        )
        userDto.isResidence -> listOf(
            MenuItem(
                text = "",
                action = { navController.navigate(MainNav.HealthRecord(userId = userDto.id!!)) },
                iconResId = R.drawable.pregnant
            ),
            MenuItem(
                text = "",
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
private fun MenuButton(
    text: String,
    onClick: () -> Unit,
    iconResId: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(105.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF6650a4))
            .clickable(onClick = onClick)
            .border(1.dp, Color(0xFF6650a4), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = "$text Icon",
                modifier = Modifier
                    .size(60.dp),
                tint = Color.White
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
        if(text.contains("Reminder")){
            Text(
                text = text,
                textAlign = TextAlign.Center,
                color =Color(0xFF6650a4),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = (FontFamily.SansSerif),
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }else{
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