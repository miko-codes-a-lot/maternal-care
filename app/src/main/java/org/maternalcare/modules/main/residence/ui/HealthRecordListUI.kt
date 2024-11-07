package org.maternalcare.modules.main.residence.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@Composable
fun HealthRecordListUI(
    navController: NavController,
    userDto: UserDto,
    currentUser: UserDto,
    conditionStatus: UserConditionDto?,
    healthRecords: List<UserBirthRecordDto>,
) {
    val isHealthConditionVisible = remember { mutableStateOf(currentUser.isAdmin) }
    val isHealthStatusVisible = rememberSaveable {
        mutableStateOf((currentUser.isResidence || currentUser.isSuperAdmin) && conditionStatus != null)
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF)),
        floatingActionButton = {
            if(currentUser.isAdmin){
                FloatingRecordsIcon(userDto, navController)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(Color(0xFFFFFFFF))
                .padding(
                    top = 50.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val isShowImage = rememberSaveable { mutableStateOf(
                currentUser.isSuperAdmin || currentUser.isAdmin || currentUser.isResidence
            ) }
            if(isShowImage.value){
                ProfileUsers(navController, userDto, currentUser, conditionStatus)
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = userDto.firstName +" "+ userDto.middleName +" "+  userDto.lastName,
                    fontSize = 23.sp,
                    fontFamily = FontFamily.SansSerif
                )
            }else{
                Image(
                    painter = painterResource(id = R.drawable.care),
                    contentDescription = "Login Logo",
                    modifier = Modifier
                        .size(110.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "No. Of Check-up", fontSize = 23.sp,
                    fontFamily = FontFamily.SansSerif
                )
            }
            if(isHealthConditionVisible.value){
                Spacer(modifier = Modifier.height(10.dp))
                ConditionStatusButton(navController, userDto)
                Spacer(modifier = Modifier.padding(bottom = 20.dp))
            }
            if(isHealthStatusVisible.value) {
                Spacer(modifier = Modifier.height(10.dp))
                PregnantConditionStatus(navController, userDto)
                Spacer(modifier = Modifier.padding(bottom = 20.dp))
            }
            if(healthRecords.isEmpty()){
                Spacer(modifier = Modifier.padding(top = 50.dp))
                Text(
                    text = "Health Record is Empty",
                    fontSize = 19.sp,
                    fontFamily = FontFamily.SansSerif,
                    color = Color.Black
                )
            }else{
                HealthButtonContainer(userDto, navController, healthRecords)
            }
        }
    }
}

@Composable
fun HealthButtonContainer(
    userId: UserDto,
    navController: NavController,
    healthRecords: List<UserBirthRecordDto>,
) {
    LazyColumn(
        modifier = Modifier
            .background(Color(0xFFFFFFFF))
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(healthRecords){  record ->
            RecordButton(
                text = "Pregnancy ${record.pregnancyOrder}",
                onClick = {
                    navController.navigate(
                        MainNav.ChooseCheckup(
                            userId = userId.id!!,
                            pregnantRecordId = record.id!!,
                            pregnantTrimesterId = record.id!!
                        )
                    )
                }
            )
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

@Composable
fun FloatingRecordsIcon(
    userDto: UserDto,
    navController: NavController,
) {
    Column(
        modifier = Modifier
            .background(Color.Transparent),
        horizontalAlignment = Alignment.End
    ) {
        FloatingActionButton(
            onClick = {
                navController.navigate(MainNav.CreateRecord(userDto.id!!))
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
private fun RecordButton(text: String, onClick: () -> Unit){
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
            fontFamily = FontFamily.Serif,
            color = Color.White
        )
    }
}

@Composable
fun ProfileUsers (
    navController: NavController,
    userDto: UserDto,
    currentUser: UserDto,
    conditionStatus: UserConditionDto? = null
){
    Box(
        Modifier
            .height(120.dp)
    ) {
        if (conditionStatus != null) {
            if(conditionStatus.isNormal){
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.Green)
                        .border(3.dp, Color(0xFF6650a4), CircleShape),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.person),
                        contentDescription = "Default placeholder",
                        modifier = Modifier
                            .size(78.dp),
                        tint = Color.White
                    )
                }
            }else if(conditionStatus.isCritical) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.Red)
                        .border(3.dp, Color(0xFF6650a4), CircleShape),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.person),
                        contentDescription = "Default placeholder",
                        modifier = Modifier
                            .size(78.dp),
                        tint = Color.White
                    )
                }
            }
        }else{
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF6650a4))
                    .border(3.dp, Color(0xFF6650a4), CircleShape),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    painter = painterResource(id = R.drawable.person),
                    contentDescription = "Default placeholder",
                    modifier = Modifier
                        .size(78.dp),
                    tint = Color.White
                )
            }
        }

        if(currentUser.isAdmin) {
            IconButton(
                onClick = {
                    navController.navigate(MainNav.ResidencePreview(userId = userDto.id!!))
                },
                modifier = Modifier
                    .size(30.dp)
                    .padding(2.dp)
                    .clip(CircleShape)
                    .align(Alignment.BottomEnd),
                colors = IconButtonDefaults.iconButtonColors(Color(0xFF6650a4)),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.visibilityon),
                    contentDescription = "Select Profile",
                    modifier = Modifier
                        .fillMaxSize()
                        .size(11.dp)
                        .padding(4.dp),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun ConditionStatusButton(
    navController: NavController,
    userDto: UserDto
) {
    ElevatedButton(
        onClick = {
            navController.navigate(MainNav.ConditionStatus(userId = userDto.id!!))
        },
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = Color(0xFF6650a4),
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
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.health),
                contentDescription = "Health Icon",
                tint = Color.White,
                modifier = Modifier
                    .size(23.dp)
            )
            Text(
                text = "Condition Status",
                fontFamily = FontFamily.Serif,
                fontSize = 18.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .weight(1f)
            )
        }
    }
}

@Composable
fun PregnantConditionStatus(
    navController: NavController,
    userDto: UserDto
) {
    ElevatedButton(
        onClick = {
            navController.navigate(MainNav.StatusPreview(userId = userDto.id!!))
        },
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = Color(0xFF6650a4),
            contentColor = Color(0xFFFFFFFF)
        ),
        modifier = Modifier
            .height(63.dp)
            .width(280.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.condition),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Pregnant Condition",
                fontFamily = FontFamily.Serif,
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }
    }
}