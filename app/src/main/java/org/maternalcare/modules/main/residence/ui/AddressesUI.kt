package org.maternalcare.modules.main.residence.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.maternalcare.R
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.residence.enum.CheckupStatus

@Preview
@Composable
fun AddressUIPreview() {
    AddressesUI(navController = rememberNavController(), isArchive = false)
}

@Composable
fun AddressesUI(navController: NavController, isArchive: Boolean = false) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .size(50.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrow),
                contentDescription = "Exit Icon",
                tint = Color.Black,
                    modifier = Modifier
                    .size(29.dp)
                    .offset(x = (10).dp, y = (45).dp)
                    .clickable {
                        navController.navigate(MainNav)
                    }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "Select Address", fontSize = 24.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 80.dp))
            Spacer(modifier = Modifier.height(20.dp))

            ListAddress(navController,isShowPercent = false)
        }
    }
}

@Composable
private fun ListButton (isShowPercent: Boolean = false, data: Map<String,Any>, onClick: () -> Unit,navController: NavController){
    ElevatedButton(onClick = onClick,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor =  Color(0xFF6650a4),
            contentColor = Color(0xFFFFFFFF)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp)
            .height(55.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Text(
            text = "${data["name"]}",
            fontSize = 17.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
        )

        if (isShowPercent) {
            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "${data["percent"]} %",
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
            )
        }
    }
}

@Composable
fun ListAddress (navController: NavController, isShowPercent: Boolean) {
    val listAddress =
        listOf(
            mapOf("name" to "Makati City", "percent" to 10),
        )
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(listAddress) { address ->
            ListButton(data = address, isShowPercent = isShowPercent, onClick = {
                navController.navigate(MainNav.Residences(CheckupStatus.ALL.name))
            }, navController = navController)
        }
    }
}