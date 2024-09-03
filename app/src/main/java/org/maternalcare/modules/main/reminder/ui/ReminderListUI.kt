package org.maternalcare.modules.main.reminder.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.residence.enum.CheckupStatus
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Preview(showSystemUi = true)
@Composable
fun ReminderListUIPreview() {
    ReminderListUI(navController = rememberNavController())
}

@Composable
fun ReminderListUI(navController: NavController) {
    Column(
        modifier = Modifier
            .height(715.dp)
            .background(Color.White)
            .fillMaxWidth(),
         horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(55.dp))
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Prenatal Check-Up Scheduled",
                fontFamily = FontFamily.Monospace,
                fontSize = 21.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth(),
                color = Color(0xFF6650a4)
            )
            Spacer(modifier = Modifier.height(10.dp))
            ScheduleList(navController)
        }
    }
}

@Composable
private fun RemindersButton (data: Map<String,Any>, onClick: () -> Unit,navController: NavController){
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
            text = "${data["dates"]}",
            fontSize = 17.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
        )
    }
}

@Composable
fun ScheduleList (navController: NavController) {

    val formatter = DateTimeFormatter.ofPattern("MMMM-dd-yyyy")

    val listAddress = listOf(
        mapOf("dates" to LocalDate.of(2024, 8, 24).format(formatter)),
        mapOf("dates" to LocalDate.of(2024, 3, 15).format(formatter)),
        mapOf("dates" to LocalDate.of(2024, 10, 5).format(formatter)),
        mapOf("dates" to LocalDate.of(2024, 8, 24).format(formatter)),
        mapOf("dates" to LocalDate.of(2024, 8, 24).format(formatter)),
        mapOf("dates" to LocalDate.of(2024, 8, 24).format(formatter)),
        mapOf("dates" to LocalDate.of(2024, 8, 24).format(formatter)),
        mapOf("dates" to LocalDate.of(2024, 8, 24).format(formatter)),
        mapOf("dates" to LocalDate.of(2024, 8, 24).format(formatter)),
        )
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(listAddress) { address ->
            RemindersButton(data = address, onClick = {
                navController.navigate(MainNav.Residences(CheckupStatus.ALL.name))
            }, navController = navController)
        }
    }
}