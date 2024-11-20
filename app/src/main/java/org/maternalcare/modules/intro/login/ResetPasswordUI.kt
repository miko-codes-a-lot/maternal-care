package org.maternalcare.modules.intro.login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import org.maternalcare.R
import org.maternalcare.modules.intro.IntroNav
import org.maternalcare.modules.intro.login.viewmodel.LoginViewModel
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.shared.ext.hashPassword

@Composable
fun ResetPasswordUI(
    email: String,
    token: String,
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    var isNewPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Reset Your Password", fontSize = 18.sp, fontFamily = FontFamily.SansSerif)
        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("New Password", color = Color.Black, fontSize = 15.sp, fontFamily = FontFamily.SansSerif) },
            visualTransformation = if (isNewPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFF6650a4),
                focusedBorderColor = Color(0xFF6650a4),
                focusedTextColor = Color.Black,
            ),
            trailingIcon = {
                IconButton(onClick = { isNewPasswordVisible = !isNewPasswordVisible }) {
                    Icon(
                        painter = painterResource(
                            id = if (isNewPasswordVisible) R.drawable.visibilityon else R.drawable.visibility_off
                        ),
                        tint = Color(0xFF6650a4),
                        contentDescription = if (isNewPasswordVisible) "Hide password" else "Show password"
                    )
                }
            }
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password", color = Color.Black, fontSize = 15.sp, fontFamily = FontFamily.SansSerif) },
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFF6650a4),
                focusedBorderColor = Color(0xFF6650a4),
                focusedTextColor = Color.Black,
            ),
            trailingIcon = {
                IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                    Icon(
                        painter = painterResource(
                            id = if (isConfirmPasswordVisible) R.drawable.visibilityon else R.drawable.visibility_off
                        ),
                        tint = Color(0xFF6650a4),
                        contentDescription = if (isConfirmPasswordVisible) "Hide password" else "Show password"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                if (newPassword == confirmPassword) {
                    viewModel.resetPassword(email, token, newPassword) { success, errorMessage ->
                        if (success) {
                            navController.navigate(IntroNav.Login)
                        } else {
                            message = errorMessage
                        }
                    }
                } else {
                    message = "Passwords do not match"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF6650a4))
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6650a4),
                contentColor = Color(0xFFFFFFFF)
            ),
        ) {
            Text(
                "Reset Password",
                fontSize = 16.sp,
                fontFamily = FontFamily.SansSerif,
                color = Color.White
            )
        }

        message?.let {
            Spacer(modifier = Modifier.height(10.dp))
            Text(it, color = Color.Red, fontSize = 15.sp)
        }
    }
}