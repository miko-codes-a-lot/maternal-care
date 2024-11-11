package org.maternalcare.modules.intro.login

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.maternalcare.R
import org.maternalcare.modules.intro.IntroNav
import org.maternalcare.modules.intro.login.model.dto.LoginDto
import org.maternalcare.modules.intro.login.viewmodel.LoginViewModel
import org.maternalcare.modules.intro.login.viewmodel.UserState
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.shared.ui.AlertLogInUI

@Preview(showSystemUi = true)
@Composable
fun LoginPreview(){
    LoginUI(navController = rememberNavController())
}

@Composable
fun LoginUI(
    userDto: UserDto? = null, 
    navController: NavController
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val listOfLabel = listOf("Email Account","Password")
        val statesValue = remember {
            listOfLabel.associateWith {
                mutableStateOf(
                    when (it) {
                        "Email Account" -> userDto?.email ?: ""
                        "Password" -> userDto?.password ?: ""
                        else -> ""
                    }
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.care),
            contentDescription = "Login Image",
            modifier = Modifier.size(140.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Login to your account",
            fontSize = 23.sp,
            fontFamily = FontFamily.Serif,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(15.dp))

        ContainerLabelAndValue( statesValue = statesValue)

        ButtonLogin(
            navController = navController,
            statesValue = statesValue
        )

        Spacer(modifier = Modifier.height(15.dp))

        TextButton(
            onClick = {
                navController.navigate(IntroNav.ForgotPassword)
            }
        ){
            Text(
                text = "Forgot password ?",
                fontSize = 15.sp,
                fontFamily = FontFamily.Serif,
                color = Color.Red
            )
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun ContainerLabelAndValue(
    statesValue: Map<String, MutableState<String>>
    ) {
    statesValue.forEach{ (label,state) ->
        UserLog(
            loginLabel = label,
            loginValue = state.value,
            onValueChange = { newInputValue ->
                state.value = newInputValue
            },
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ButtonLogin(
        navController: NavController,
        statesValue: Map<String, MutableState<String>>
) {
    val vm = UserState.current
    val coroutineScope = rememberCoroutineScope()
    var showButton by remember { mutableStateOf(true) }

    val loginViewModel: LoginViewModel = hiltViewModel()
    val showAlert = rememberSaveable { mutableStateOf(false) }

    if (showAlert.value) {
        AlertLogInUI(
            onDismiss = { showAlert.value = false },
        )
    }

    if(showButton) {
        ElevatedButton(
            onClick = {
                val loginDto = LoginDto(
                    username = statesValue["Email Account"]?.value ?: "",
                    password = statesValue["Password"]?.value ?: "",
                )
                val isSuccess = loginViewModel.login(loginDto)
                Log.d("Login","${loginDto}")
                if (isSuccess) {
                    coroutineScope.launch {
                        showButton = false
                        vm.signIn()

                        navController.navigate(MainNav.Menu) {
                            popUpTo(0)
                        }
                    }
                } else {
                    showAlert.value = true
                }
            },
            modifier = Modifier
                .width(275.dp)
                .height(55.dp), shape = RectangleShape,
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = Color(0xFF6650a4),
                contentColor = Color(0xFFFFFFFF)
            ),
            enabled = true,
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 3.dp
            )
        ) {
            if(vm.isLoggedIn){
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(24.dp),
                    color = Color(0xFF6650a4),
                )
            }else{
                Text(
                    text = "Login",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }else{
        CircularProgressIndicator(
            modifier = Modifier
                .padding(top = 15.dp)
                .size(40.dp),
            color = Color(0xFF6650a4),
        )
    }
}

@Composable
fun UserLog(
    loginLabel: String,
    loginValue: String,
    onValueChange: (String) -> Unit,
) {
    val isPasswordField = loginLabel == "Password"
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .width(275.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = loginValue,
            onValueChange = {
                onValueChange(it)
            },
            label = {
                Text(text = loginLabel,
                    fontFamily = FontFamily.Serif,
                    color = Color(0xFF6650a4)
                )
            },
            textStyle = TextStyle(
                color = Color(0xFF6650a4),
                fontSize = 16.sp,
                fontFamily = FontFamily.Serif,
            ),
            leadingIcon = {
                if(loginLabel.contains("Email Account")){
                    Icon( imageVector = Icons.Default.Email, contentDescription = null, tint = Color(0xFF6650a4))
                }else {
                    Icon( imageVector = Icons.Default.Lock, contentDescription = null, tint = Color(0xFF6650a4))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF6650a4),
                unfocusedBorderColor = Color(0xFF6650a4),
                cursorColor = Color.Gray
            ),
            visualTransformation = if (isPasswordField && !isPasswordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            trailingIcon = {
                if (isPasswordField) {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            painter = painterResource(
                                id = if (isPasswordVisible) R.drawable.visibilityon else R.drawable.visibility_off
                            ),
                            tint = Color(0xFF6650a4)
                            ,
                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                }
            }
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
}