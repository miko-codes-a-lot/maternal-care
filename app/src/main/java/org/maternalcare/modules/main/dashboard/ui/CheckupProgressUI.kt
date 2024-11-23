package org.maternalcare.modules.main.dashboard.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.maternalcare.modules.main.residence.ui.ListAddress
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.viewmodel.UserViewModel

@Preview
@Composable
fun CheckupProgressUIPreview() {
    CheckupProgressUI(rememberNavController(), true, userViewModel = hiltViewModel(), isArchive = true)
}

@SuppressLint("DefaultLocale")
@Composable
fun CheckupProgressUI(
    navController: NavController,
    isComplete: Boolean,
    isArchive: Boolean,
    userViewModel: UserViewModel = hiltViewModel()
) {
    val addressCheckupPercentages = userViewModel.getCompleteCheckupPercentages(isArchive = isArchive)
    val overallCompletedPercentage = addressCheckupPercentages["Overall Completed Address Percentage"] ?: 0.0
    val getAllListAddressCheckups = userViewModel.getAllListAddressCheckup()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val completePercentage = "${String.format("%.2f", overallCompletedPercentage)}%"
            val incompletePercentage = "${String.format("%.2f", 100 - overallCompletedPercentage)}%"
            Spacer(modifier = Modifier.padding(top = 50.dp))

            AverageStatusContainer(
                completePercentage = completePercentage,
                incompletePercentage = incompletePercentage,
                isComplete = isComplete
            )
            Spacer(modifier = Modifier.height(30.dp))

            ListAddress(
                navController = navController,
                isShowPercent = true,
                isComplete = isComplete,
                addressFetchAll = getAllListAddressCheckups,
                isArchive = isArchive,
                isDashboard = true
            )
        }
    }
}

@Composable
fun AverageStatusContainer(completePercentage: String, incompletePercentage: String, isComplete: Boolean) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color(0xFF6650a4),
            contentColor = Color.White
        ),
        modifier = Modifier.size(width = 295.dp, height = 125.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val result = if (isComplete) {
                AverageStats(label = "Complete", value = completePercentage)
            } else {
                AverageStats(label = "Incomplete", value = incompletePercentage)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                result
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
            fontSize = 19.sp,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.SansSerif,
        )

        Spacer(modifier = Modifier.width(50.dp))

        Text(
            text = value,
            modifier = Modifier.padding(10.dp),
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.SansSerif,
        )
    }
}