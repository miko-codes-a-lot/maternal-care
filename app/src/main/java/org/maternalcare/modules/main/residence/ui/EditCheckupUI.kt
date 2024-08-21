package org.maternalcare.modules.main.residence.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Preview
@Composable
fun EditCheckupUIPreview() {
    EditCheckupUI(rememberNavController())
}

@Composable
fun EditCheckupUI(navController: NavController) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(480.dp)
                .border(2.dp, color = Color.DarkGray, shape = RoundedCornerShape(15.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Checkup Details",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
                Spacer(modifier = Modifier.height(16.dp))

                ContainerValue()

                Spacer(modifier = Modifier.height(13.dp))
                Button(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .height(40.dp)
                ) {
                    Text("Submit")
                }
            }
        }
    }
}

@Composable
fun ContainerValue() {
    val initialValues = listOf(
        "Height",
        "Weight",
        "Last Menstrual Period",
        "Expected Due Date",
        "Age Of Gestation",
        "Nutritional Status",
        "Schedule of Next Check-up"
    )

    val states = remember {
        initialValues.associateWith { mutableStateOf("") }
    }

    states.forEach { (label, state) ->
        EditDetailsList(labelContainer = label, sampleValue = state.value) { newValue ->
            state.value = newValue
        }
    }
}

@Composable
fun EditDetailsList(labelContainer: String, sampleValue: String, onValueChange: (String) -> Unit) {
    var text by remember { mutableStateOf(sampleValue) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(9.dp)
                .height(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = labelContainer,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(" : ", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                TextField(
                    value = text,
                    onValueChange = {
                        text = it
                        onValueChange(it)
                    },
                    placeholder = { Text("Enter value") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}