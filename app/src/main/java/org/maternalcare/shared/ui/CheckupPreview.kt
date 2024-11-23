package org.maternalcare.shared.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Preview(showSystemUi = true)
@Composable
fun CheckUpPreview() {
    val sampleStatesValue = mapOf(
        "key1" to remember { mutableStateOf("Sample Value 1") },
        "key2" to remember { mutableStateOf("Sample Value 2") }
    )
    CheckupPreview(onDismiss = {}, onConfirm = {}, statesValue = sampleStatesValue)
}

@Composable
fun CheckupPreview(
    onDismiss : () -> Unit,
    onConfirm: () -> Unit,
    statesValue: Map<String, MutableState<String>>
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = Color.White
            ),
            modifier = Modifier
                .padding(16.dp)
                .border(
                    4.dp,
                    color = Color(0xFF6650a4),
                    shape = RoundedCornerShape(15.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Checkup Preview",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.SansSerif,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp, top = 16.dp),
                    textAlign = TextAlign.Center
                )
                statesValue.forEach { (label, state) ->
                    val displayValue = when (label) {
                        "Date of Check-up", "Last Menstrual Period", "Next Check-up" -> formatDate(state.value)
                        else -> state.value
                    }
                    PreviewItem(label, displayValue)
                }


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6650a4),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            "Submit",
                            fontFamily = FontFamily.SansSerif,
                            color = Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .padding(vertical = 6.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Gray
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            "Cancel",
                            fontFamily = FontFamily.SansSerif,
                            color = Color.Gray,
                            fontSize = 16.sp,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PreviewItem(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 13.dp, bottom = 13.dp),
        ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(" : ", fontWeight = FontWeight.Bold)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = value,
                    fontSize = 16.sp,
                    color = Color.Black
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