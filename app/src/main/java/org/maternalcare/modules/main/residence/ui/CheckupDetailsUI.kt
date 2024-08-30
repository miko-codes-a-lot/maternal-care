package org.maternalcare.modules.main.residence.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.maternalcare.R
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
        containerColor = Color.White,
        floatingActionButton = {
            FloatingMainIcon(navController)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NumberOfCheckUp()
            Spacer(modifier = Modifier.height(18.dp))
            Column{
                label.zip(value).forEach { (labelItem, valueItem) ->
                    CheckupDetailsList(labelContainer = labelItem, sampleValue = valueItem)
                }
            }
            Spacer(modifier = Modifier.height(52.dp))
        }
    }
}

@Composable
fun NumberOfCheckUp() {
    val numberOfCheckUp = listOf( "1st CheckUp" )
    Row(
        modifier = Modifier.fillMaxWidth(),
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
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = labelContainer,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                )
                Text(" :  ", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.padding(horizontal = 1.dp))
                Column(
                   modifier = Modifier,
                    horizontalAlignment = Alignment.Start
                ){
                    Text(
                        text = sampleValue,
                        fontFamily = FontFamily.SansSerif,
                        modifier = Modifier
                            .padding( start = 5.dp)
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth(),
                        thickness = 1.dp,
                        color = Color.Gray
                    )
                }
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FloatingMainIcon(navController: NavController) {
    ParentFloatingIcon(navController = navController)
}

@Composable
fun ParentFloatingIcon(navController: NavController) {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf(
        painterResource(id = R.drawable.editicon)
    )

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(horizontalAlignment = Alignment.End) {
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it }) + expandVertically(),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it }) + shrinkVertically()
            ) {
                LazyColumn(
                    modifier = Modifier.offset(y = 4.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    items(items.size) { index ->
                        when (index) {
                            0 -> ChildIcon(
                                painter = items[index],
                                navController = navController
                            ) {
                                navController.navigate(MainNav.EditCheckup)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
            FloatingActionButton(
                onClick = { expanded = !expanded },
                containerColor = Color(0xFF6650a4),
                contentColor = Color(0xFFFFFFFF),
                shape = CircleShape,
                modifier = Modifier
                    .size(72.dp)
                    .offset(x = (-13).dp, y = (-8).dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Add",
                    modifier = Modifier
                        .rotate(if (expanded) 45f else 0f)
                        .size(30.dp)
                )
            }
        }
    }
}

@Composable
fun ChildIcon(painter: Painter, navController: NavController, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier.size(52.dp)
                .offset(x = (-23).dp, y = (-19).dp),
            containerColor = Color(0xFF6650a4),
            contentColor = Color(0xFFFFFFFF)
        ) {
            Icon(
                painter = painter,
                contentDescription = "childIcon",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}