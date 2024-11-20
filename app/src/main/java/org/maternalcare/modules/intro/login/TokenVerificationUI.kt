package org.maternalcare.modules.intro.login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.maternalcare.modules.intro.IntroNav
import org.maternalcare.modules.intro.login.viewmodel.LoginViewModel

@Preview(showBackground = true)
@Composable
fun TokenVerificationUIPreview() {
    TokenVerificationUI(
        email = "william.henry.harrison@example-pet-store.com",
        navController = rememberNavController()
    )
}

@Composable
fun TokenVerificationUI(
    email: String,
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var token by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    Log.d("TokenVerificationUIF", "User Email: $email")

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Enter the token sent to your email", fontSize = 17.sp, fontFamily = FontFamily.SansSerif)
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = token,
            onValueChange = { token = it },
            label = { Text("Token", color = Color.Black, fontSize = 15.sp, fontFamily = FontFamily.SansSerif) },
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
                viewModel.verifyToken(email, token) { success, errorMessage ->
                    if (success) {
                        Log.d("TokenVerificationUIF", "Success User Email: $email, Token: $token")
                        navController.navigate(IntroNav.ResetPassword(email, token))
                    } else {
                        Log.d("TokenVerificationUIF", "Error User Email: $email, Token: $token")
                        message = errorMessage ?: "Token verification failed"
                    }
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
                "Verify Token",
                fontSize = 16.sp,
                fontFamily = FontFamily.SansSerif,
                color = Color.White
            )
        }

        message?.let {
            Spacer(modifier = Modifier.height(10.dp))
            Text(it, color = Color.Red, fontSize = 15.sp, fontFamily = FontFamily.SansSerif)
        }
    }
}
