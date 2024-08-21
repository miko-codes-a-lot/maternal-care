package org.maternalcare.modules.main.dashboard.ui

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.maternalcare.R
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.residence.ui.ListAddress

@Preview
@Composable
fun CheckupProgressUIPreview() {
    CheckupProgressUI(rememberNavController())
}

@Composable
fun CheckupProgressUI(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
       Icon(
            painter = painterResource(id = R.drawable.arrow),
            contentDescription = "Exit Icon",
            tint = Color.Black,
            modifier = Modifier
                .size(30.dp)
                .offset(x = (-160).dp, y = (28).dp)
                .clickable {
                    navController.navigate(MainNav.Dashboard)
                }
       )
        Spacer(modifier = Modifier.height(55.dp))

        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //Sample value
            val completePercentage = "80%"
            val incompletePercentage = "20%"

            AverageStatusContainer(
                completePercentage = completePercentage,
                incompletePercentage = incompletePercentage
            )

            Spacer(modifier = Modifier.height(30.dp))

            ListAddress(navController = navController, isShowPercent = true)
        }
    }
}

@Composable
fun AverageStatusContainer(completePercentage: String, incompletePercentage: String) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color(0xFF6650a4),
            contentColor = Color.White
        ),
        modifier = Modifier.size(width = 290.dp, height = 110.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AverageStats(label = "Complete  ", value = completePercentage,)
                AverageStats(label = "Incomplete", value = incompletePercentage)
            }
        }
    }
}

@Composable
fun AverageStats(label: String, value: String) {
    Row {

        Text(
            text = label,
            modifier = Modifier.padding(10.dp),
            fontSize = 17.sp,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.Monospace,
        )

        Spacer(modifier = Modifier.width(75.dp))

        Text(
            text = value,
            modifier = Modifier.padding(10.dp),
            fontSize = 17.sp,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.Monospace,
        )
    }
}