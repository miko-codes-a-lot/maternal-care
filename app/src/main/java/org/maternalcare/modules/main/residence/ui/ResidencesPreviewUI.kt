package org.maternalcare.modules.main.residence.ui

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
import androidx.compose.material3.HorizontalDivider
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
import org.maternalcare.modules.main.user.model.dto.UserDto
import java.text.SimpleDateFormat
import java.util.Locale

@Preview(showSystemUi = true)
@Composable
fun ResidencesPrevUI() {
    ResidencesPreviewUI(user = UserDto())
}

@Composable
fun ResidencesPreviewUI(
    user: UserDto,
) {
    val statesValue = remember(user) {
        listOf(
            "First Name" to user.firstName,
            "Middle Name" to (user.middleName ?: ""),
            "Last Name" to user.lastName,
            "Email" to (user.email ?: ""),
            "Mobile Number" to (user.mobileNumber ?: ""),
            "Date Of Birth" to dateFormat(user.dateOfBirth),
            "User Type" to if (user.isResidence) "Resident" else "Non-Resident",
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
        Text(text = "Profile",
            fontFamily = FontFamily.Serif,
            fontSize = 25.sp,
            modifier = Modifier
                .padding(bottom = 3.dp, top = 7.dp)
        )
        ResidencesData(statesValue)

        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Composable
fun ResidencesData(stateValues: Map<String, MutableState<String>>) {
    Column {
        stateValues.forEach { (label, states) ->
            TextFieldContain(textLabel = label, textValue = states.value)
        }
    }
}

@Composable
fun TextFieldContain(textLabel: String, textValue: String) {
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

fun dateFormat(dateString: String): String {
    return try {
        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val displayFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val dates = isoFormat.parse(dateString)
        displayFormat.format(dates)
    } catch (e: Exception) {
        "Select Date"
    }
}