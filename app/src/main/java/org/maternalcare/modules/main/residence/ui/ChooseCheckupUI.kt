package org.maternalcare.modules.main.residence.ui

import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.maternalcare.R
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.user.model.dto.UserBirthRecordDto
import org.maternalcare.modules.main.user.model.dto.UserConditionDto
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.model.dto.UserTrimesterRecordDto

@Composable
fun ChooseCheckupUI(
    navController: NavController,
    currentUser: UserDto,
    userDto: UserDto,
    conditionStatus: UserConditionDto?,
    pregnantRecordId: UserBirthRecordDto,
    trimesterRecord: List<UserTrimesterRecordDto>,
) {
    val isStatusVisible = rememberSaveable {
        mutableStateOf(currentUser.isResidence || currentUser.isSuperAdmin || conditionStatus != null)
    }
    val isConditionVisible = remember { mutableStateOf(currentUser.isAdmin || isStatusVisible.value) }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF)),
        floatingActionButton = {
            if (currentUser.isAdmin && trimesterRecord.size != 3) {
                FloatingTrimesterRecords(navController, userDto, pregnantRecordId)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(Color.White)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isConditionVisible.value) {
                Spacer(modifier = Modifier.height(15.dp))
                ImmunizationRecordButton(navController, userDto, pregnantRecordId)
            }
            if(trimesterRecord.isEmpty()){
                Spacer(modifier = Modifier.padding(top = 50.dp))
                Text(
                    text = "Trimester Record is Empty",
                    fontSize = 19.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.SansSerif,
                    color = Color.Black,
                )
            }else{
                TrimesterButtonContainer(
                    navController = navController,
                    userId = userDto,
                    pregnantRecordId = pregnantRecordId,
                    trimesterRecordId = trimesterRecord
                )
            }
        }
    }
}

@Composable
fun TrimesterButtonContainer(
    navController: NavController,
    userId: UserDto,
    pregnantRecordId: UserBirthRecordDto,
    trimesterRecordId: List<UserTrimesterRecordDto>,
) {
    LazyColumn(
        modifier = Modifier
            .background(Color(0xFFFFFFFF))
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(trimesterRecordId){  record ->
            TrimesterRecordButton(
                text = "Trimester ${record.trimesterOrder}",
                onClick = {
                    navController.navigate(
                        MainNav.TrimesterCheckUpList(
                            userId = userId.id!!,
                            pregnantRecordId = pregnantRecordId.id!!,
                            trimesterId = record.id!!
                        )
                    )
                }
            )
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

@Composable
fun ImmunizationRecordButton(
    navController: NavController,
    userDto: UserDto,
    pregnantRecordId: UserBirthRecordDto
) {
    ElevatedButton(
        onClick = {
            navController.navigate(
                MainNav.ImmunizationRecord(
                    userId = userDto.id!!,
                    pregnantRecordId = pregnantRecordId.id!!
                )
            )
        },
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = Color(0xFF6650a4),
            contentColor = Color(0xFFFFFFFF)
        ),
        modifier = Modifier
            .height(63.dp)
            .width(280.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.record),
                contentDescription = "Record Icon",
                tint = Color.White,
                modifier = Modifier
                    .size(23.dp)
            )
            Text(
                text = "Immunization Record",
                fontFamily = FontFamily.Serif,
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(start = 15.dp)
                        .weight(1f)
            )
        }
    }
}

@Composable
fun ButtonContainer(text: String, onClick: () -> Unit) {
    ElevatedButton(
        onClick = onClick,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = Color(0xFF6650a4),
            contentColor = Color(0xFFFFFFFF)
        ),
        modifier = Modifier
            .height(63.dp)
            .width(280.dp)
    ) {
        Text(
            text = text, fontFamily = FontFamily.Serif,
            fontSize = 20.sp
        )
    }
}

@Composable
fun FloatingTrimesterRecords(
    navController: NavController,
    userId: UserDto,
    pregnantRecordId: UserBirthRecordDto,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        horizontalAlignment = Alignment.End
    ) {
        FloatingActionButton(
            onClick = {
                navController.navigate(MainNav.CreateTrimester(userId.id!!, pregnantRecordId = pregnantRecordId.id!!))
            },
            containerColor = Color(0xFF6650a4),
            contentColor = Color(0xFFFFFFFF),
            shape = CircleShape,
            modifier = Modifier
                .size(75.dp)
                .offset(x = (-5).dp, y = (-7).dp)
        ){
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add",
                modifier = Modifier
                    .size(30.dp)
            )
        }
    }
}

@Composable
private fun TrimesterRecordButton(text: String, onClick: () -> Unit){
    ElevatedButton(onClick = onClick,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor =  Color(0xFF6650a4),
            contentColor = Color(0xFFFFFFFF)
        ),
        modifier = Modifier
            .width(280.dp)
            .height(63.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.Serif
        )
    }
}