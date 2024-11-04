package org.maternalcare.modules.main.residence.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.maternalcare.modules.main.user.model.dto.UserBirthRecordDto
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.model.dto.UserTrimesterRecordDto
import org.maternalcare.modules.main.user.viewmodel.UserViewModel

@Composable
fun CreateTrimesterRecordUI(
    userDto: UserDto,
    trimesterRecord: UserTrimesterRecordDto,
    navController: NavController,
    pregnantRecordDto: UserBirthRecordDto,
    pregnantTrimesterRecords: List<UserTrimesterRecordDto>
) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val trimesterRecords = listOf("Trimester 1", "Trimester 2", "Trimester 3")
        var expanded by remember { mutableStateOf(false) }
        var currentTrimesterIndex by remember { mutableIntStateOf(-1) }
        var textFieldSize by remember { mutableStateOf(Size.Zero) }
        var isSubmitClicked by remember { mutableStateOf(false) }
        var validationMessage by remember { mutableStateOf("") }
        val icon = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

        val pregnancySpecificTrimesterRecords = pregnantTrimesterRecords.filter {
            it.pregnancyUserId == pregnantRecordDto.id
        }

        val selectedTrimesterOrder = currentTrimesterIndex + 1
        val isDuplicateTrimester = currentTrimesterIndex != -1 && pregnancySpecificTrimesterRecords.any {
            it.trimesterOrder == selectedTrimesterOrder
        }

        if (isSubmitClicked && validationMessage.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = validationMessage,
                    color = Color.Red,
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 2.dp, color = Color(0xFF6650a4), shape = RectangleShape)
                .height(60.dp)
                .background(Color.White)
                .clickable { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.White)
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates -> textFieldSize = coordinates.size.toSize() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (currentTrimesterIndex == -1) "Select Trimester" else trimesterRecords.getOrNull(currentTrimesterIndex) ?: "",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6650a4),
                    fontFamily = FontFamily.SansSerif,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = icon,
                    tint = Color(0xFF6650a4),
                    contentDescription = null
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(Color.White)
                    .padding(start = 5.dp, end = 5.dp)
                    .fillMaxWidth()
            ) {
                trimesterRecords.forEachIndexed { index, trimester ->
                    DropdownMenuItem(
                        onClick = {
                            currentTrimesterIndex = index
                            trimesterRecord.trimesterOrder = index + 1
                            expanded = false
                        },
                        text = {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(if (index == currentTrimesterIndex) Color(0xFF6650a4) else Color.White)
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = trimester,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (index == currentTrimesterIndex) Color.White else Color(0xFF6650a4),
                                    fontFamily = FontFamily.SansSerif
                                )
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        ButtonTrimesterSaved(
            userId = userDto.id!!,
            navController = navController,
            trimesterRecord = trimesterRecord,
            pregnantRecordId = pregnantRecordDto.id!!,
            trimesterOrder = trimesterRecord.trimesterOrder,
            isDuplicateTrimester = isDuplicateTrimester,
            isTrimesterSelected = currentTrimesterIndex != -1,
            onSubmitClick = { isSubmitClicked = true },
            setValidationMessage = { validationMessage = it }
        )
    }
}

@Composable
fun ButtonTrimesterSaved(
    userId: String,
    navController: NavController,
    trimesterRecord: UserTrimesterRecordDto,
    pregnantRecordId: String,
    trimesterOrder: Int,
    isDuplicateTrimester: Boolean,
    isTrimesterSelected: Boolean,
    onSubmitClick: () -> Unit,
    setValidationMessage: (String) -> Unit
) {
    val userViewModel: UserViewModel = hiltViewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Button(
        onClick = {
            onSubmitClick()
            if (!isTrimesterSelected) {
                setValidationMessage("Please select a trimester.")
            } else if (isDuplicateTrimester) {
                setValidationMessage("Trimester $trimesterOrder already exists for this pregnancy.")
            } else {
                setValidationMessage("")
                val userTrimesterRecord = UserTrimesterRecordDto(
                    id = trimesterRecord.id,
                    pregnancyUserId = pregnantRecordId,
                    trimesterUserId = userId,
                    trimesterOrder = trimesterOrder
                )
                scope.launch {
                    try {
                        val result = userViewModel.upsertTrimesterRecord(userTrimesterRecord)
                        if (result.isSuccess) {
                            navController.popBackStack()
                        } else {
                            Log.e("TrimesterRecordUI", "Error: ${result.exceptionOrNull()}")
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            "Error saving Trimester record data: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF6650a4))
            .height(55.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF6650a4),
            contentColor = Color(0xFFFFFFFF),
        )
    ) {
        Text(
            "Submit",
            fontSize = 16.sp,
            fontFamily = FontFamily.SansSerif,
            color = Color.White
        )
    }
}