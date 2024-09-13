package org.maternalcare.modules.main.settings.ui

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.maternalcare.R
import org.maternalcare.modules.intro.IntroNav
import org.maternalcare.modules.intro.login.viewmodel.UserState
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.service.UserService
import org.maternalcare.modules.main.user.ui.UserImageUI
import org.maternalcare.modules.main.user.ui.getBytesFromUri
import org.maternalcare.modules.main.user.viewmodel.UserViewModel

@Composable
fun SettingsUI(navController: NavController,
               currentUser: UserDto,
               userService: UserService = hiltViewModel<UserViewModel>().userService
) {
    val vm = UserState.current
    val coroutineScope = rememberCoroutineScope()
    var showButton by remember { mutableStateOf(true) }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(currentUser.id) {
        profileImageUri = currentUser.imageBase64?.let { Uri.parse(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Profile(navController, currentUser, userService)
        Spacer(
            modifier = Modifier
                .height(30.dp)
        )
        if (showButton) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        showButton = false
                        vm.signOut()
                        navController.navigate(IntroNav.Login) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                },
                modifier = Modifier.padding(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF6650a4)
                )
            ) {
                if (vm.isBusy) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color(0xFF6650a4),
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.logout),
                        contentDescription = "Logout",
                        tint = Color.Red,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Logout",
                        fontSize = 17.sp,
                        color = Color(0xFF6650a4),
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }
        } else {
            CircularProgressIndicator(
                modifier = Modifier.size(40.dp),
                color = Color(0xFF6650a4),
            )
        }
    }
}

@Composable
fun Profile(
        navController: NavController,
        currentUser: UserDto,
        userService: UserService
){
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var selectedProfileImageUri by remember { mutableStateOf<Uri?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserImageUI(
            onImageSelected = { newUri ->
                selectedProfileImageUri = newUri
                coroutineScope.launch {
                    val byteArray = getBytesFromUri(context, newUri)
                    Log.d("getBytesFromUri", "ByteArray: ${byteArray?.size ?: "null"}")
                    if (byteArray != null) {
                        userService.saveProfilePicture(currentUser.id!!, byteArray)
                    }
                }
            },
            currentUserId = currentUser.id!!,
            userService = userService
        )

        Spacer(modifier = Modifier.padding(vertical = 15.dp))

        UserDetails(currentUser)

        Spacer(modifier = Modifier.padding(vertical = 5.dp))

        Setting(navController)
    }
}

@Composable
fun UserDetails(currentUser: UserDto) {
    val userDetails = listOf(
        currentUser.firstName,
        currentUser.middleName,
        currentUser.lastName
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        userDetails.forEach { fullName ->
            if (fullName != null) {
                Text(
                    text = fullName,
                    fontSize = 17.sp,
                    fontFamily = FontFamily.SansSerif,
                    color = Color(0xFF6650a4),
                    modifier = Modifier.padding(horizontal = 3.dp)
                )
            }
        }
    }
}

@Composable
fun Setting(navController: NavController){
    val settingsMenu = listOf(
        SettingItem(text = "Email"){
            navController.navigate("${MainNav.EditSettings}/email")
        },
        SettingItem(text = "Password"){
            navController.navigate("${MainNav.EditSettings}/password")
        }
    )
    Column {
        settingsMenu.forEach { menuSettings ->
            Spacer(modifier = Modifier.height(10.dp))
            SettingButton(text = menuSettings.text, onClick = menuSettings.action)
        }
    }
}

@Composable
fun SettingButton(text: String, onClick: () -> Unit) {
    Button(onClick = onClick,
        modifier = Modifier
            .width(290.dp)
            .height(70.dp)
            .padding(5.dp)
            .border(2.dp, color = Color(0xFF6650a4), shape = RoundedCornerShape(5.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color(0xFF6650a4)
        )
    ) {
        Row(
            modifier = Modifier
        ){
            Text(text = text, fontSize = 16.sp, fontFamily = FontFamily.SansSerif)
            Spacer(modifier = Modifier.weight(0.1f))
            Icon(painter = painterResource(id = R.drawable.editicon)
                ,contentDescription = null,
                modifier = Modifier.size(20.dp))
        }
    }
}
data class SettingItem(val text: String, val action: () -> Unit)