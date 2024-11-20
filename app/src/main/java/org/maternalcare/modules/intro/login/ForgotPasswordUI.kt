package org.maternalcare.modules.intro.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import org.maternalcare.modules.intro.IntroNav
import org.maternalcare.modules.intro.login.viewmodel.LoginViewModel
import org.maternalcare.modules.intro.login.viewmodel.UserState

@Composable
fun ForgotPasswordUI(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(Color.White)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Enter your email to reset password", fontSize = 17.sp, fontFamily = FontFamily.SansSerif)
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = Color.Black, fontSize = 15.sp, fontFamily = FontFamily.SansSerif) },
            modifier = Modifier
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFF6650a4),
                focusedBorderColor = Color(0xFF6650a4),
                focusedTextColor = Color.Black,
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                if (email.isNotBlank()) {
                    isLoading = true
                    viewModel.sendPasswordResetToken(email) { success, errorMessage ->
                        isLoading = false
                        if (success) {
                            navController.navigate(IntroNav.TokenVerification(email))
                        }
                    }
                } else {
                    message = "Email cannot be blank."
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF6650a4))
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6650a4),
                contentColor = Color(0xFFFFFFFF),
            ),
        ) {

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
            } else {
                Text(
                    "Send Reset Token",
                    fontSize = 16.sp,
                    fontFamily = FontFamily.SansSerif,
                    color = Color.White
                )
            }



        }
        message?.let {
            Text(
                it,
                color = if (it.contains("success")) Color.Green else Color.Red,
                fontSize = 15.sp,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}
