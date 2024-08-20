package org.maternalcare.modules.main.dashboard.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.maternalcare.R
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.residence.enum.CheckupStatus

@Composable
fun DashboardUI(navController: NavController) {
    Surface {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
               painter = painterResource(id = R.drawable.arrow),
               contentDescription = "Exit Icon",
               tint = Color.Black,
               modifier = Modifier
               .size(40.dp)
               .align(Alignment.TopStart)
               .padding(start = 12.dp, top = 12.dp)
               .offset(x = 10.dp, y = 25.dp)
               .clickable {
                   navController.navigate(MainNav.Menu)
               }
            )
        }
        Column (
             modifier = Modifier.fillMaxSize(),
             verticalArrangement = Arrangement.Center,
             horizontalAlignment = Alignment.CenterHorizontally
        ) {
             Image(painter = painterResource(id = R.drawable.care),
                   contentDescription = "Logo Image",
                   modifier = Modifier.size(180.dp)
             )
             Spacer(modifier = Modifier.height(10.dp))
             Text(text = "Dashboard", fontSize = 30.sp, fontWeight = FontWeight.Bold)
             Spacer(modifier = Modifier.height(14.dp))

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
            navController.navigate(MainNav.MonitorCheckupProgressUI)
        }

        // Incomplete
        Spacer(modifier = Modifier.height(25.dp))
        DashboardButton(text = "Incomplete",iconResId = R.drawable.clear) {
            navController.navigate(MainNav.Addresses(CheckupStatus.INCOMPLETE.name))
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
            fontSize = 19.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
        Spacer(modifier = Modifier.width(10.dp))
        iconResId?.let {
           Image(
               painter = painterResource(id = it),
               contentDescription = null,
               modifier = Modifier.size(35.dp),
               colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
           )
        }
    }
}
