package org.maternalcare.modules.main.residence.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.maternalcare.modules.main.MainNav

@Preview(showSystemUi = true)
@Composable
fun CheckPreview() {
    CheckupDetailsUI(navController = rememberNavController())
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CheckupDetailsUI(navController: NavController) {
    Scaffold(
        floatingActionButton = {
            ParentFloatingIcon(navController)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NumberOfCheckUp()
            LazyColumn(
                modifier = Modifier
                    .background(Color.White)
                    .height(470.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                items(label.zip(value)) { (labelItem, valueItem) ->
                    CheckupDetailsList(labelContainer = labelItem, sampleValue = valueItem)
                }
            }
        }
    }
}

@Composable
fun NumberOfCheckUp() {
    val numberOfCheckUp = listOf( "1st CheckUp" )
    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        numberOfCheckUp.forEach { checkUp ->
            Text(
                text = checkUp,
                fontSize = 22.sp,
                fontFamily = FontFamily.SansSerif
            )
        }
    }
}

@Composable
private fun CheckupDetailsList(labelContainer: String, sampleValue: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = labelContainer,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                fontSize = 17.sp
            )
            Text(" :  ", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.padding(horizontal = 1.dp))
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = sampleValue,
                    fontFamily = FontFamily.SansSerif,
                    modifier = Modifier
                        .padding(start = 5.dp),
                    fontSize = 18.sp
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = Color.Gray
                )
            }
        }
    }
}

val label = listOf(
    "Name","Date of Birth","Age","Address","Household Number",
    "Blood Pressure","Height","Weight","Last Menstrual Period",
    "Expected Due Date","Age Of Gestation","Nutritional Status",
    "Schedule of Next Check-up"
)

val value = listOf(
    "John Doe","01/01/1990","34","1234 Elm St","5678","120/80",
    "170 cm","70 kg","N/A","01/01/2024","20 weeks","Good","01/09/2024"
)

@Composable
fun ParentFloatingIcon(navController: NavController) {
    Column(
        modifier = Modifier
            .background(Color.Transparent),
        horizontalAlignment = Alignment.End
    ) {
        FloatingActionButton(
            onClick = { navController.navigate(MainNav.EditCheckup) },
            containerColor = Color(0xFF6650a4),
            contentColor = Color(0xFFFFFFFF),
            shape = CircleShape,
            modifier = Modifier
                .size(72.dp)
                .offset(x = (-7).dp, y = (-5).dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Navigate",
                modifier = Modifier
                    .size(30.dp)
            )
        }
    }
}
