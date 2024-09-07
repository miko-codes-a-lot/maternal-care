package org.maternalcare.modules.main.settings.ui

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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.navigation.NavController
import org.maternalcare.R
import org.maternalcare.modules.main.MainNav

@Composable
fun EditSettingsUI(navController: NavController, settingType: String) {
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
                "email" -> EmailEdit(navController)
                "password" -> PasswordEdit(navController)
                else -> Text(text = "Invalid setting type")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailEdit(navController: NavController) {
    var email: String by remember { mutableStateOf("Sample@gmail.com") }
    var editEmail: String by remember { mutableStateOf("") }
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
                    label = { Text(text = "Email", fontFamily = FontFamily.SansSerif, color = Color(0xFF6650a4), fontSize = 14.sp) },
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
                        Text(text = email,color = (Color(0xFF6650a4)),
                            fontSize = 15.sp,
                            fontFamily = FontFamily.SansSerif,
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
                        email = editEmail
                        navController.navigate(MainNav.Settings)
                    }
                    editEmailMode = true
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
fun PasswordEdit(navController: NavController) {
    val fieldLabels = listOf( "Current Password", "New Password", "Re-Type New Password")
    val fieldStates = remember { fieldLabels.associateWith { mutableStateOf("") } }

    var currentPasswordError: String? by remember { mutableStateOf(null) }
    var newPasswordError: String? by remember { mutableStateOf(null) }
    val simulatedCurrentPassword = "sample"
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
                label = { Text(text = label, color = Color(0xFF6650a4), fontFamily = FontFamily.SansSerif, fontSize = 14.sp) },
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
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF6650a4),
                    unfocusedBorderColor = Color(0xFF6650a4),
                    cursorColor = Color.Gray
                ),
                supportingText = {
                    when (label) {
                        "Current Password" -> currentPasswordError?.let { Text(it, color = Color.Red) }
                        "New Password", "Re-Type New Password" -> newPasswordError?.let { Text(it, color = Color.Red) }
                        else -> null
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        Button(
            onClick = {
                val currentPassword = fieldStates["Current Password"]?.value
                val newPassword = fieldStates["New Password"]?.value
                val retypePassword = fieldStates["Re-Type New Password"]?.value

                when {
                    currentPassword != simulatedCurrentPassword -> {
                        currentPasswordError = "Current password is incorrect"
                    }
                    newPassword != retypePassword -> {
                        newPasswordError = "New passwords do not match"
                    }
                    else -> {
                        fieldStates.forEach { (label, state) -> state.value = "" }
                        navController.navigate(MainNav.Settings)
                    }
                }
            },
            modifier = Modifier
                .width(280.dp)
                .height(54.dp)
                .background(Color(0xFF6650a4))
                .border(2.dp, color = Color(0xFF6650a4), shape = RoundedCornerShape(5.dp)),
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