package org.maternalcare.shared.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.maternalcare.modules.main.menu.ui.CheckUpDateContainer
import org.maternalcare.modules.main.menu.ui.TextContainer

@Preview(showSystemUi = true)
@Composable
fun ReminderAlertUIPreview() {
    ReminderAlertUI(onDismiss = {})
}

@Composable
fun ReminderAlertUI (
    onDismiss : () -> Unit,
    listOfText: List<String> = listOf("Reminder","Your next check-up will be on."),
    isReminderAlert: Boolean = false,
    isError: Boolean = false
) {
    Dialog(
        onDismissRequest = {},
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
                .fillMaxWidth(0.95f)
                .heightIn(min = 100.dp, max = 300.dp)
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
                Spacer(modifier = Modifier.height(10.dp))
                listOfText.forEach { text ->
                    TextContainer(text = text)
                }
                if (!isError) {
                    CheckUpDateContainer()
                }
                Row(
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Button(
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6650a4),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .height(32.dp)
                    ) {
                        Text(
                            text = "Close",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}