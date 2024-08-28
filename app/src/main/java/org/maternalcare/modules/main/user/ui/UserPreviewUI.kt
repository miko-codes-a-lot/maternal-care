package org.maternalcare.modules.main.user.ui

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.user.model.UserDto


@Composable
fun UserPreviewUI(title: String,navController: NavController, user: UserDto, onSave: suspend (UserDto) -> Unit)
{
    val coroutineScope = rememberCoroutineScope()
    val isSaving = remember { mutableStateOf(false) }
    val statesValue = remember(user) {
        listOf(
            "First Name" to user.firstName,
            "Middle Name" to user.middleName,
            "Last Name" to user.lastName,
            "Email" to user.email,
            "Mobile Number" to user.mobileNumber,
            "Date Of Birth" to user.dateOfBirth,
            "User Type" to (if (user.isSuperAdmin) "SuperAdmin" else if (user.isAdmin) "Admin" else "Residence"),
            "Active" to (if (user.isActive) "Yes" else "No")
        ).associate { (label, value) -> label to mutableStateOf(value) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title,
            fontFamily = FontFamily.Serif,
            fontSize = 24.sp,
            modifier = Modifier
                .padding(bottom = 3.dp, top = 7.dp)
        )

        ViewData(statesValue)

        ButtonPreview(navController, user, coroutineScope, onSave, isSaving)

        Spacer(modifier = Modifier.padding(top = 9.dp))

        TextButton(onClick = {navController.navigate(MainNav.CreateUser)}) {
            Text(text = "cancel", modifier = Modifier,fontSize = 15.sp)
        }
    }
}

@Composable
fun ViewData(stateValues: Map<String, MutableState<String>>) {
    Column {
        stateValues.forEach { (label, states) ->
            TextContainer(textLabel = label, textValue = states.value)
        }
    }
}

@Composable
fun TextContainer(textLabel: String, textValue: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 22.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
            text = textLabel,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            fontSize = 18.sp,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(" : ",fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = textValue,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp),
                            fontSize = 17.sp,
                             fontFamily = FontFamily.SansSerif,
                    )
                    Divider(
                        color = Color.Gray,
                        thickness = 1.dp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun ButtonPreview(navController: NavController, user: UserDto, coroutineScope: CoroutineScope,
        onSave: suspend (UserDto) -> Unit, isSaving: MutableState<Boolean>) {
    Button(onClick = {
        if (!isSaving.value) {
            isSaving.value = true
            coroutineScope.launch {
                try {
                    onSave(user)
                    navController.navigate(MainNav.User){
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                } catch (e: Exception) {
                    Log.e("ButtonPreview", "Error during save: ${e.message}")
                } finally {
                    isSaving.value = false
                }
            }
        }
    },
        enabled = !isSaving.value,
        modifier = Modifier
            .width(360.dp)
            .padding(top = 27.dp)
            .height(54.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF6650a4),
            contentColor = Color(0xFFFFFFFF)
        )
    ) {
        Text(text = "Confirm", fontSize = 17.sp)
    }
}