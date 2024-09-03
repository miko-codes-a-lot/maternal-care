package org.maternalcare.modules.main.residence.ui

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Preview(showSystemUi = true)
@Composable
fun ChooseCheckupUIPrev() { ChooseCheckupUI(rememberNavController()) }

@Composable
fun ChooseCheckupUI(navController: NavController) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
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
            CheckUpNavigationButton(navController )
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
fun CheckUpNavigationButton(navController: NavController){
    val listOfCheckup = listOf(
        SelectionCheckUp(text = "1st Checkup") {
            navController.navigate(MainNav.CheckupDetails)
        },
        SelectionCheckUp(text = "2nd Checkup"){
            navController.navigate(MainNav.CheckupDetails)
        },
        SelectionCheckUp(text = "3rd Checkup"){
            navController.navigate(MainNav.CheckupDetails)
        },
        SelectionCheckUp(text = "4th Checkup"){
            navController.navigate(MainNav.CheckupDetails)
        }
    )
    Column (
        modifier = Modifier.padding(20.dp)
    ) {
        listOfCheckup.forEach{ list ->
            Spacer(modifier = Modifier.height(15.dp))
            ButtonContainer(onClick = list.action, text = list.text )
        }
    }
}