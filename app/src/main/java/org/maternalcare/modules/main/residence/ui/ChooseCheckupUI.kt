package org.maternalcare.modules.main.residence.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.maternalcare.R
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.user.model.dto.UserDto

@Preview(showSystemUi = true)
@Composable
fun ChooseCheckupUIPreview() {
    ChooseCheckupUI(
        navController = rememberNavController(),
        currentUser = UserDto(),
        userDto = UserDto()
    )
}

@SuppressLint("ProduceStateDoesNotAssignValue")
@Composable
fun ChooseCheckupUI(
    navController: NavController,
    currentUser: UserDto,
    userDto: UserDto
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val isShowImage = rememberSaveable { mutableStateOf( currentUser.isSuperAdmin || currentUser.isAdmin) }

            if(isShowImage.value){
                ProfileUsers(navController, userDto, currentUser)
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = userDto.firstName +" "+ userDto.middleName +" "+  userDto.lastName,
                    fontSize = 23.sp,
                    fontFamily = FontFamily.SansSerif
                )
            }else{
                Image(
                    painter = painterResource(id = R.drawable.care),
                    contentDescription = "Login Image",
                    modifier = Modifier.size(110.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "No. Of Check-up", fontSize = 23.sp,
                    fontFamily = FontFamily.SansSerif
                )
            }
            Spacer(modifier = Modifier.padding(10.dp))
            ConditionStatusButton(navController,userDto)
            Spacer(modifier = Modifier.height(15.dp))
            ImmunizationRecordButton(navController, userDto)
            CheckUpNavigationButton(navController, userDto)
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
            containerColor =  Color(0xFF6650a4),
            contentColor = Color(0xFFFFFFFF)
        ),
        modifier = Modifier
            .height(63.dp)
            .width(280.dp)

    ) {
        Text(text = "Condition Status", fontFamily = FontFamily.Serif,
            fontSize = 20.sp
        )
    }
}

@Composable
fun ImmunizationRecordButton(
    navController: NavController,
    userDto: UserDto
) {
    ElevatedButton(
        onClick = {
            navController.navigate(MainNav.ImmunizationRecord(userId = userDto.id!!))
        },
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor =  Color(0xFF6650a4),
            contentColor = Color(0xFFFFFFFF)
        ),
        modifier = Modifier
            .height(63.dp)
            .width(280.dp)
    ) {
        Text(text = "Immunization Record", fontFamily = FontFamily.Serif,
            fontSize = 20.sp
        )
    }
}

@Composable
fun ButtonContainer(text : String, onClick:() -> Unit){
    ElevatedButton(onClick = onClick,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor =  Color(0xFF6650a4),
            contentColor = Color(0xFFFFFFFF)
        ),
        modifier = Modifier
            .height(63.dp)
            .width(280.dp)
    ) {
        Text(text = text, fontFamily = FontFamily.Serif,
            fontSize = 20.sp
        )
    }
}

data class SelectionCheckUp (val text: String, val action: () -> Unit )
@Composable
fun CheckUpNavigationButton(
    navController: NavController,
    userDto: UserDto,
) {
    val listOfCheckup = listOf(
        SelectionCheckUp(text = "Checkup 1") {
            navController.navigate(MainNav.CheckupDetails(userId = userDto.id!!, checkupNumber = 1))
        },
        SelectionCheckUp(text = "Checkup 2") {
            navController.navigate(MainNav.CheckupDetails(userId = userDto.id!!, checkupNumber = 2))
        },
        SelectionCheckUp(text = "Checkup 3") {
            navController.navigate(MainNav.CheckupDetails(userId = userDto.id!!, checkupNumber = 3))
        },
        SelectionCheckUp(text = "Checkup 4") {
            navController.navigate(MainNav.CheckupDetails(userId = userDto.id!!, checkupNumber = 4))
        }
    )
    Column(
        modifier = Modifier
            .height(319.dp),
        verticalArrangement = Arrangement.Center
    ) {
        listOfCheckup.forEach { checkup ->
            Spacer(modifier = Modifier.height(15.dp))
            ButtonContainer(onClick = checkup.action, text = checkup.text)
        }
    }
}


@Composable
fun ProfileUsers (navController: NavController, userDto: UserDto, currentUser: UserDto ){
    Box(
        Modifier.height(120.dp)
    ) {
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