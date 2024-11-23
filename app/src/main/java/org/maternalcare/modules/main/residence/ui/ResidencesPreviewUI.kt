package org.maternalcare.modules.main.residence.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.user.model.dto.UserDto
import java.text.SimpleDateFormat
import java.util.Locale

@Preview(showSystemUi = true)
@Composable
fun ResidencesPrevUI() {
    ResidencesPreviewUI(
        navController = rememberNavController(),
        currentUser = UserDto(),
        userDto = UserDto()
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ResidencesPreviewUI(
    navController: NavController,
    currentUser: UserDto,
    userDto: UserDto,
) {
    val statesValue = remember(userDto) {
        listOf(
            "First Name" to (userDto.firstName ?: ""),
            "Middle Name" to (userDto.middleName ?: ""),
            "Last Name" to (userDto.lastName ?: ""),
            "Address" to (userDto.address ?: ""),
            "Email" to (userDto.email ?: ""),
            "Mobile Number" to (userDto.mobileNumber ?: ""),
            "Date Of Birth" to (formatDates(userDto.dateOfBirth?: "")),
            "Active" to (if (userDto.isActive) "Yes" else "No")
        ).associate { (label, value) -> label to mutableStateOf(value) }
    }
    Scaffold(
        floatingActionButton = {
            if (currentUser.isAdmin) {
                FloatingIconPreview(navController, userDto)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Profile",
                fontFamily = FontFamily.SansSerif,
                fontSize = 25.sp,
                modifier = Modifier
                    .padding(bottom = 3.dp, top = 7.dp)
            )
            ResidencesData(
                stateValues = statesValue,
                currentUser = currentUser
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun ResidencesData(
    stateValues: Map<String, MutableState<String>>,
    currentUser: UserDto,
    ) {
    Column {
        stateValues.forEach { (label, state) ->
            TextFieldContain(
                textLabel = label,
                textState = state
            )
        }
    }
}

@Composable
fun TextFieldContain(
    textLabel: String,
    textState: MutableState<String>,
) {
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
            Text(" : ", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = textState.value,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp),
                        fontSize = 17.sp,
                        fontFamily = FontFamily.SansSerif,
                    )
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun FloatingIconPreview(
    navController: NavController,
    userDto: UserDto
) {
    Column(
        modifier = Modifier
            .background(Color.Transparent),
        horizontalAlignment = Alignment.End
    ) {
        FloatingActionButton(
            onClick = {
                navController.navigate(MainNav.EditUser(userDto.id!!))
            },
            containerColor = Color(0xFF6650a4),
            contentColor = Color(0xFFFFFFFF),
            shape = CircleShape,
            modifier = Modifier
                .size(72.dp)
                .offset(x = (-7).dp, y = (5).dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Navigate",
                modifier = Modifier
                    .size(30.dp)
            )
        }
    }
}

fun formatDates(dateString: String?): String {
    if (dateString.isNullOrBlank()) {
        return "No Date Available"
    }
    return try {
        val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val displayFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = isoFormatter.parse(dateString) ?: throw Exception("ISO format error")
        displayFormatter.format(date)
    } catch (e: Exception) {
        try {
            val simpleFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val displayFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val date = simpleFormatter.parse(dateString) ?: throw Exception("Simple date format error")
            displayFormatter.format(date)
        } catch (e: Exception) {
            "Invalid Date"
        }
    }
}