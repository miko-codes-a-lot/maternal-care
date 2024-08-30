package org.maternalcare.modules.main.residence.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

@Preview
@Composable
fun EditCheckupUIPreview() {
    EditCheckupUI(rememberNavController())
}

@Composable
fun EditCheckupUI(navController: NavController) {
    val initialValues = listOf(
        "Blood Pressure","Height", "Weight", "Last Menstrual Period",
        "Expected Due Date", "Age Of Gestation", "Nutritional Status",
        "Next Check-up"
    )
    val states = remember { initialValues.associateWith { mutableStateOf("") } }

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ContainerValue(states)
        Spacer(modifier = Modifier.height(8.dp))
        ButtonSaveEdit(navController, states)
    }
}

@Composable
fun ContainerValue(statesValue: Map<String, MutableState<String>>) {
    statesValue.forEach { (labels, states) ->
        EditDetailsList(labelContainer = labels, sampleValue = states.value) { newValues ->
            states.value = newValues
        }
    }
}

@Composable
fun EditDetailsList(
    labelContainer: String,
    sampleValue: String,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = labelContainer,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                fontSize = 17.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(" : ", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            TextField(
                value = sampleValue,
                onValueChange = { newValue ->
                    onValueChange(newValue)
                },
                placeholder = { Text("Enter value") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(51.dp),
                isError = sampleValue.isNotEmpty() && !isValidText(sampleValue),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    errorIndicatorColor = Color.Red
                )
            )
        }
    }
}

fun isValidText(inputValue: String): Boolean {
    return inputValue.matches(Regex("[a-zA-Z]+"))
}

@Composable
fun ButtonSaveEdit(
    navController: NavController,
    states: Map<String, MutableState<String>>
){
    Button(
        onClick = {
            val allFieldsValid = states.values.all { it.value.isNotBlank() }
            if (allFieldsValid) {
                navController.navigate(MainNav.CheckupDetails)
            }
        },
        modifier = Modifier
            .width(360.dp)
            .padding(top = 27.dp)
            .height(54.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF6650a4),
            contentColor = Color(0xFFFFFFFF)
        )
    ) {
        Text(text = "Confirm", fontSize = 17.sp)
    }
}