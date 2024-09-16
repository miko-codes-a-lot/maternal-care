package org.maternalcare.modules.main.residence.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import org.maternalcare.R
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.user.model.dto.UserCheckupDto
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.viewmodel.UserViewModel

@SuppressLint("ProduceStateDoesNotAssignValue")
@Composable
fun ChooseCheckupUI(navController: NavController, currentUser: UserDto) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        val userViewModel: UserViewModel = hiltViewModel()

        val checkupList by produceState<List<UserCheckupDto>>(emptyList(), userViewModel) {
            value = userViewModel.fetchUserCheckUp()
        }
        Log.d("ChooseCheckupUI", "Fetched Checkup List: ${checkupList.size}")

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
            if (checkupList.isNotEmpty()) {
                CheckUpNavigationButton(navController, checkupList, currentUser)
            } else {
                Text(text = "No checkups available")
            }

        }
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
    checkupDto: List<UserCheckupDto>,
    currentUser: UserDto
){
    val listOfCheckup = checkupDto.flatMap { checkup ->
        List(checkup.checkup) { index ->
            SelectionCheckUp(text = "${index + 1}${getOrdinal(index + 1)} Checkup") {
                if (checkup.userId == currentUser.id) {
                    Log.d("CheckUpNavigationButton", "User ID for ${checkup.id}, ${currentUser.id}")
                    navController.navigate(MainNav.CheckupDetails(checkUpId = checkup.id))
                } else {
                    Log.d("CheckUpNavigationButton", "User ID does not match for checkup ${checkup.id}")
                }
            }
        }
    }

    Column (
        modifier = Modifier
            .height(380.dp)
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {
        listOfCheckup.forEach{ checkup  ->
            Spacer(modifier = Modifier.height(15.dp))
            ButtonContainer(onClick = checkup .action, text = checkup .text )
        }
    }
}
fun getOrdinal(number: Int): String {
    return when (number) {
        1 -> "st"
        2 -> "nd"
        3 -> "rd"
        else -> "th"
    }
}