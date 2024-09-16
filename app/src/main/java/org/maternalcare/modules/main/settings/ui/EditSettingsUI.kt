package org.maternalcare.modules.main.settings.ui

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.maternalcare.R
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.viewmodel.UserViewModel
import org.mindrot.jbcrypt.BCrypt

@Composable
fun EditSettingsUI(navController: NavController, settingType: String, currentDto: UserDto) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (settingType) {
                "email" -> EmailEdit(navController, currentDto)
                "password" -> PasswordEdit(navController, currentDto)
                else -> Text(text = "Invalid setting type")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailEdit(navController: NavController, currentDto: UserDto) {
    val viewModel: UserViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope()

    var editEmail by remember { mutableStateOf(currentDto.email ?: "") }
    var editEmailMode by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(280.dp)
        ) {
            if (editEmailMode) {
                OutlinedTextField(
                    value = editEmail,
                    onValueChange = { editEmail = it },
                    label = { Text(text = "Email",
                        fontFamily = FontFamily.SansSerif,
                        color = Color(0xFF6650a4), fontSize = 14.sp)
                    },
                    textStyle = TextStyle(
                        color = Color(0xFF6650a4),
                        fontSize = 14.sp,
                        fontFamily = FontFamily.SansSerif,
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6650a4),
                        unfocusedBorderColor = Color(0xFF6650a4),
                        cursorColor = Color.Gray
                    ),
                )
            } else {
                Box(
                    modifier = Modifier
                        .width(280.dp)
                        .border(
                            1.dp,
                            color = Color(0xFF6650a4),
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                    )
                    {
                        Text(text = "Email :", fontSize = 15
                            .sp,
                            fontFamily = FontFamily.SansSerif,
                            color = (Color(0xFF6650a4))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = currentDto.email ?: "No email",
                            fontSize = 15.sp,
                            fontFamily = FontFamily.SansSerif,
                            color = Color(0xFF6650a4)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 7.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Button(
                onClick = {
                    if (editEmailMode) {
                        currentDto.email = editEmail
                        coroutineScope.launch {
                            try {
                                val result = viewModel.upsertUser(currentDto.copy(email = currentDto.email), currentDto)
                                if (result.isSuccess) {
                                    navController.navigate(MainNav.Settings)
                                } else {
                                    Log.e("EmailEdit", "Failed to update email")
                                }
                            } catch (error: Exception) {
                                Log.e("EmailEdit", "Failed to update email: ${error.message}")
                            }
                        }
                    }
                    editEmailMode = !editEmailMode
                },
                modifier = Modifier
                    .width(280.dp)
                    .height(54.dp)
                    .background(Color(0xFF6650a4))
                    .border(1.dp, color = Color(0xFF6650a4), shape = RoundedCornerShape(5.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = if (!editEmailMode) "Change" else "Save",
                    fontSize = 14.sp, fontFamily = FontFamily.Serif
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordEdit(navController: NavController, currentDto: UserDto) {
    val viewModel: UserViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope()

    val fieldLabels = listOf( "Current Password", "New Password", "Re-Type New Password")
    val fieldStates = remember { fieldLabels.associateWith { mutableStateOf("") } }

    var currentPasswordError: String? by remember { mutableStateOf(null) }
    var newPasswordError: String? by remember { mutableStateOf(null) }

    val passwordVisibilityStates = remember {
        fieldLabels.associateWith { mutableStateOf(false) }.toMutableMap()
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        fieldLabels.forEach { label ->
            val passwordVisible = passwordVisibilityStates[label]?.value ?: false
            OutlinedTextField(
                value = fieldStates[label]?.value ?: "",
                onValueChange = { newValue ->
                    fieldStates[label]?.value = newValue
                    if (label == "Current Password") {
                        currentPasswordError = null
                    } else if (label == "New Password" || label == "Re-Type New Password") {
                        newPasswordError = null
                    }
                },
                label = {
                    Text(
                        text = label,
                        color = Color(0xFF6650a4),
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 14.sp)
                        },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisibilityStates[label]?.value = !passwordVisible
                    }) {
                        Icon(
                            painter = if (passwordVisible) painterResource(R.drawable.visibilityon) else painterResource(R.drawable.visibility_off),
                                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = Color(0xFF6650a4)
                        )
                    }
                },
                isError = when (label) {
                    "Current Password" -> currentPasswordError != null
                    "New Password", "Re-Type New Password" -> newPasswordError != null
                    else -> false
                },
                textStyle = TextStyle(
                    color = Color(0xFF6650a4),
                    fontSize = 14.sp,
                    fontFamily = FontFamily.SansSerif,
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF6650a4),
                    unfocusedBorderColor = Color(0xFF6650a4),
                    cursorColor = Color.Gray
                ),
                supportingText = {
                    when (label) {
                        "Current Password" -> currentPasswordError?.let { Text(it, color = Color.Red) }
                        "New Password", "Re-Type New Password" -> newPasswordError?.let { Text(it, color = Color.Red) }
                        else -> Spacer(modifier = Modifier.height(0.dp))
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        Button(
            onClick = {
                val currentPassword = fieldStates["Current Password"]?.value?.trim()
                val newPassword = fieldStates["New Password"]?.value?.trim()
                val retypePassword = fieldStates["Re-Type New Password"]?.value?.trim()

                when {
                    currentPassword == null || !BCrypt.checkpw(currentPassword, currentDto.password) -> {
                        currentPasswordError = "Current password is incorrect"
                    }
                    newPassword != retypePassword -> {
                        newPasswordError = "New passwords do not match"
                    }
                    else -> {
                        val hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt())

                        coroutineScope.launch {
                            try {
                                val result = viewModel.upsertUser(currentDto.copy(password = hashedNewPassword), currentDto)
                                if (result.isSuccess) {
                                    navController.navigate(MainNav.Settings)
                                } else {
                                    Log.e("PasswordEdit", "Failed to update password")
                                }
                            } catch (error: Exception) {
                                Log.e("PasswordEdit", "Failed to update password: ${error.message}")
                            }
                        }

                        fieldStates.forEach { (_, state) -> state.value = "" }
                    }
                }
            },
            modifier = Modifier
                .width(280.dp)
                .height(54.dp)
                .background(Color(0xFF6650a4))
                .border(
                    2.dp,
                    color = Color(0xFF6650a4),
                    shape = RoundedCornerShape(5.dp)
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Save",
                fontSize = 15.sp,
                fontFamily = FontFamily.Serif
            )
        }
    }
}