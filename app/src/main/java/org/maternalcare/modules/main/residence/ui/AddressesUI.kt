package org.maternalcare.modules.main.residence.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.residence.enum.CheckupStatus
import org.maternalcare.modules.main.residence.viewmodel.ResidenceViewModel
import org.maternalcare.modules.main.user.model.dto.AddressDto

@Preview
@Composable
fun AddressUIPreview() {
    AddressesUI(navController = rememberNavController(), isArchive = false)
}

@Composable
fun AddressesUI(
    navController: NavController,
    isArchive: Boolean = false,
    isDashboard: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
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
                text = "Select Address",
                fontFamily = FontFamily.Monospace,
                fontSize = 23.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth(),
                color = Color(0xFF6650a4)
            )
            Spacer(modifier = Modifier.height(10.dp))
             ListAddress(
                 navController,
                 isArchive = isArchive,
                 isShowPercent = false,
                 addressFetchAll = mapOf(),
                 isComplete = true,
                 isDashboard = isDashboard
             )
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun ListButton (
    isShowPercent: Boolean = false,
    addressDto: AddressDto,
    onClick: () -> Unit,
    navController: NavController,
    totalToShow: Int
){
    ElevatedButton(
        onClick = onClick,
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
            text = addressDto.name,
            fontSize = 17.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
        )
        if (isShowPercent && totalToShow > 0.0) {
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "- " +String.format("%d", totalToShow),
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
            )
        }
    }
}
@Composable
fun ListAddress(
    navController: NavController,
    isShowPercent: Boolean = false,
    isComplete: Boolean,
    isArchive: Boolean = false,
    addressFetchAll: Map<String, Map<String, Int>>,
    isDashboard: Boolean
) {
    val residenceViewModel: ResidenceViewModel = hiltViewModel()
    val addresses = residenceViewModel.fetchAddresses()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(addresses) { address ->
            val total = addressFetchAll[address.name] ?: mapOf("Complete" to 0, "Incomplete" to 0)
            val completeTotal = total["Complete"] ?: 0
            val incompleteTotal = total["Incomplete"] ?: 0
            val totalToShow = if (isComplete) completeTotal else incompleteTotal

            ListButton(
                addressDto = address,
                isShowPercent = isShowPercent,
                onClick = {
                    val checkupStatus =
                        if (isComplete)
                            CheckupStatus.COMPLETE.name
                        else
                            CheckupStatus.INCOMPLETE.name

                    val residenceRoute = MainNav.Residences(
                        status = checkupStatus,
                        isArchive = isArchive,
                        addressId = address.id,
                        isDashboard = isDashboard
                    )
                    navController.navigate(residenceRoute)
                },
                navController = navController,
                totalToShow = totalToShow
            )
        }
    }
}