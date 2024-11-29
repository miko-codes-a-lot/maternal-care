package org.maternalcare.modules.main.residence.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import org.maternalcare.R
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
    var debouncedQuery by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchMode by remember { mutableStateOf(false) }
    var selectedAddress by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(searchQuery) {
        delay(500L)
        debouncedQuery = searchQuery
    }

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
            if(!isSearchMode){
                Box(
                    modifier = Modifier
                        .clickable { isSearchMode = true }
                        .fillMaxWidth()
                        .height(55.dp),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.search_icon),
                        contentDescription = "Search",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(26.dp)
                            .offset(x = (-120).dp)
                            .clickable { isSearchMode = true }
                    )
                    Text(
                        text = "Select Address",
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center,
                        fontSize = 23.sp,
                        modifier = Modifier
                            .clickable { isSearchMode = true }
                    )
                }
            }else{
                SearchAddressIcon(
                    searchQuery = searchQuery,
                    onSearchQueryChanged = { searchQuery = it },
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
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
                isDashboard = isDashboard,
                searchQuery = debouncedQuery,
                selectedAddress = selectedAddress
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
            .height(60.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.location_pin),
                contentDescription = "Address Icon",
                tint = Color.White,
                modifier = Modifier
                    .size(25.dp)
                    .clickable {
                        navController.navigate(MainNav.Map(addressDto.id!!))
                    }
            )
            Text(
                text = addressDto.name,
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier
                        .weight(0.9f)
            )
            if (isShowPercent && totalToShow > 0.0) {
                Text(
                    text = "% " +String.format("%d", totalToShow),
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                )
            }else{
                Spacer(modifier = Modifier.padding(end = 18.dp))
            }
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
    isDashboard: Boolean,
    searchQuery: String = "",
    selectedAddress: String? = null
) {
    val residenceViewModel: ResidenceViewModel = hiltViewModel()
    val addresses = residenceViewModel.fetchAddresses()

    val filteredAddresses = remember(searchQuery, selectedAddress) {
        addresses.filter { address ->
            (selectedAddress == null || address.name == selectedAddress) &&
                    address.name.contains(searchQuery, ignoreCase = true)
        }
    }

    LazyColumn(
        modifier = Modifier
            .padding(bottom = 45.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(filteredAddresses) { address ->
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

@Composable
fun SearchAddressIcon(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = searchQuery,
        onValueChange = { onSearchQueryChanged(it) },
        leadingIcon = {
            IconButton(onClick = { } ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchQueryChanged("") }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Clear Search",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Black,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedBorderColor = Color.Black,
            disabledBorderColor = Color.Gray,
            errorBorderColor = Color.Red,
            cursorColor = Color.Black
        ),
        placeholder = {
            Text(
                text = "Search address...",
                color = Color.Black,
                fontFamily = FontFamily.SansSerif,
                fontSize = 16.sp
            )
        }
    )
}