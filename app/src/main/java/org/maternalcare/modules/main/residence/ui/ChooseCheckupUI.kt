package org.maternalcare.modules.main.residence.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.maternalcare.R
import org.maternalcare.modules.main.MainNav

@Composable
fun ChooseCheckupUI(navController: NavController) {
    Surface() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.care),
                contentDescription = "Login Image",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Checkup List",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Cursive
            )

            Spacer(modifier = Modifier.height(10.dp))

            CheckupList(navController)
        }
    }
}

private data class SelectionLists(val text: String, val action: () -> Unit)

@Composable
private fun CheckupList(navController: NavController) {
    var selectionList = listOf(
        SelectionLists(text = "1st Checkup"){
            navController.navigate(MainNav.CheckupDetails)
        },
        SelectionLists(text = "2nd Checkup"){
            navController.navigate(MainNav.CheckupDetails)
        },
        SelectionLists(text = "3rd Checkup"){
            navController.navigate(MainNav.CheckupDetails)
        },
        SelectionLists(text = "4th Checkup"){
            navController.navigate(MainNav.CheckupDetails)
        },
    )
    Column {
        selectionList.forEach { selectionList ->
            Spacer(modifier = Modifier.height(20.dp))
            CheckupButton(text = selectionList.text, onClick = selectionList.action)
        }
    }
}

@Composable
private fun CheckupButton(text: String, onClick: () -> Unit) {
    ElevatedButton(onClick = onClick,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor =  Color(0xFF6650a4),
            contentColor = Color(0xFFFFFFFF)
        ),
        modifier = Modifier
            .width(280.dp)
            .height(60.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Text(
            text = text,
            fontSize = 17.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
    }
}
