package org.maternalcare.modules.main.dashboard.ui

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.maternalcare.R
import org.maternalcare.modules.main.MainNav

@Composable
fun DashboardUI(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column (
             modifier = Modifier
                 .padding(16.dp)
                 .fillMaxSize(),
             verticalArrangement = Arrangement.Center,
             horizontalAlignment = Alignment.CenterHorizontally
        ) {
             Image(painter = painterResource(id = R.drawable.care),
                   contentDescription = "Logo Image",
                   modifier = Modifier.size(140.dp)
             )
             Spacer(modifier = Modifier.height(15.dp))
             Text(
                 text = "Dashboard",
                 fontSize = 25.sp,
                 fontFamily = FontFamily.Serif
             )
             Spacer(modifier = Modifier.height(15.dp))

             DashboardMenu(navController)
        }
    }
}

@Composable
private fun DashboardMenu (navController: NavController) {
    Column {
        //Complete
        Spacer(modifier = Modifier.height(10.dp))
        DashboardButton(text = "Complete",iconResId = R.drawable.check) {
            navController.navigate(MainNav.MonitoringCheckup(isComplete = true, dashboard = true))
        }

        // Incomplete
        Spacer(modifier = Modifier.height(25.dp))
        DashboardButton(text = "Incomplete",iconResId = R.drawable.clear) {
            navController.navigate(MainNav.MonitoringCheckup(isComplete = false, dashboard = false))
        }
    }
}

@Composable
private fun DashboardButton(text: String,iconResId: Int? = null, onClick: () -> Unit) {
    ElevatedButton(
        onClick = onClick,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = Color(0xFF6650a4),
              contentColor = Color.White
        ),
        modifier = Modifier
            .width(280.dp)
            .height(70.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        
        Text(
            text = text,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
        Spacer(modifier = Modifier.width(10.dp))
        iconResId?.let {
           Image(
               painter = painterResource(id = it),
               contentDescription = null,
               modifier = Modifier.size(32.dp),
               colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
           )
        }
    }
}
