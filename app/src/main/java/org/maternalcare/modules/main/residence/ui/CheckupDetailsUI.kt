    package org.maternalcare.modules.main.residence.ui

    import android.annotation.SuppressLint
    import androidx.compose.foundation.background
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.offset
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
    import androidx.compose.foundation.shape.CircleShape
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.Edit
    import androidx.compose.material3.FloatingActionButton
    import androidx.compose.material3.HorizontalDivider
    import androidx.compose.material3.Icon
    import androidx.compose.material3.Scaffold
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
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
    import org.maternalcare.modules.main.user.model.dto.UserCheckupDto
    import org.maternalcare.modules.main.user.model.dto.UserDto
    import java.time.LocalDate
    import java.time.format.DateTimeFormatter
    import java.time.format.DateTimeParseException

    @Preview(showSystemUi = true)
    @Composable
    fun CheckPreview() {
        CheckupDetailsUI(
            navController = rememberNavController(),
            currentUser = UserDto(),
            currentCheckup = UserCheckupDto(),
        )
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun CheckupDetailsUI(
        navController: NavController,
        currentUser: UserDto,
        currentCheckup: UserCheckupDto
    ) {

        currentCheckup.id.ifEmpty { "No Checkup Available" }

        val fullName = listOfNotNull(
            currentUser.firstName,
            currentUser.middleName,
            currentUser.lastName)
            .joinToString(" ")
        val userLabel = listOf(
            "Name", "Date of Birth", "Mobile Number", "Address",
        )

        val userValue = listOf(
            fullName,
            formatIsoDate(currentUser.dateOfBirth),
            currentUser.mobileNumber ,
            currentUser.address
        )

        val checkupLabels = listOf(
            "Blood Pressure", "Height", "Weight", "Date Of Checkup",
            "Last Menstrual Period", "Schedule of Next Check-up"
        )

        val checkupValues = listOf(
            currentCheckup.bloodPressure.toString(),
            currentCheckup.height.toString(),
            currentCheckup.weight.toString(),
            currentCheckup.dateOfCheckUp,
            currentCheckup.lastMenstrualPeriod,
            currentCheckup.scheduleOfNextCheckUp
        )

        Scaffold(
            floatingActionButton = {
                ParentFloatingIcon(navController,
                    currentUser,
                    currentCheckup
                )
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
                NumberOfCheckUp(currentCheckup)

                LazyColumn(
                    modifier = Modifier
                        .background(Color.White)
                        .height(205.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    items(userLabel.zip(userValue)) { (labelItems, valueItems) ->
                        CheckupDetailsList(labelContainer = labelItems, sampleValue = valueItems)
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .background(Color.White)
                        .height(300.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    items(checkupLabels.zip(checkupValues)) { (labelItem, valueItem) ->
                        CheckupDetailsList(labelContainer = labelItem, sampleValue = valueItem)
                    }
                }
            }
        }
    }

    fun formatIsoDate(isoDate: String): String? {
        return try {
            isoDate.let {
                val parsedDate = LocalDate.parse(isoDate.substring(0, 10))
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                parsedDate.format(formatter)
            } ?: "No Date of Birth"
        } catch (e: DateTimeParseException) {
            "Invalid Date Format"
        }
    }

    @Composable
    fun NumberOfCheckUp(currentCheckup: UserCheckupDto) {
        val numberOfCheckUp = when (currentCheckup.checkup) {
            1 -> "1st CheckUp"
            2 -> "2nd CheckUp"
            3 -> "3rd CheckUp"
            else -> "${currentCheckup.checkup}th CheckUp"
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = numberOfCheckUp,
                fontSize = 22.sp,
                fontFamily = FontFamily.SansSerif
            )
        }
    }

    @Composable
    private fun CheckupDetailsList(
        labelContainer: String,
        sampleValue: String?
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .height(50.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = labelContainer,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 17.sp
                )
                Text(" :  ", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.padding(horizontal = 1.dp))
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.Start
                ) {
                    if (sampleValue != null) {
                        Text(
                            text = sampleValue,
                            fontFamily = FontFamily.SansSerif,
                            modifier = Modifier
                                .padding(start = 5.dp),
                            fontSize = 18.sp
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = Color.Gray
                    )
                }
            }
        }
    }

    @Composable
    fun ParentFloatingIcon(
        navController: NavController,
        currentUser: UserDto,
        currentCheckup: UserCheckupDto
    ) {
        Column(
            modifier = Modifier
                .background(Color.Transparent),
            horizontalAlignment = Alignment.End
        ) {
            FloatingActionButton(
                onClick = {
                    navController.navigate("edit_checkup/${currentUser.id}/${currentCheckup.id}")
                          },
                containerColor = Color(0xFF6650a4),
                contentColor = Color(0xFFFFFFFF),
                shape = CircleShape,
                modifier = Modifier
                    .size(72.dp)
                    .offset(x = (-7).dp, y = (-5).dp)
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