package org.maternalcare.modules.main.residence.ui

import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import org.maternalcare.MainActivity
import org.maternalcare.R
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.chat.ui.PlaceholderImage
import org.maternalcare.modules.main.chat.ui.decodeBase64ToBitmap
import org.maternalcare.modules.main.residence.enum.CheckupStatus
import org.maternalcare.modules.main.residence.viewmodel.ResidenceViewModel
import org.maternalcare.modules.main.user.model.dto.AddressDto
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.service.UserReport
import org.maternalcare.modules.main.user.ui.resizeBitmap
import org.maternalcare.modules.main.user.viewmodel.UserViewModel
import org.maternalcare.shared.ext.toObjectId


@Preview(showSystemUi = true)
@Composable
fun ResidencesPrev() {
    ResidencesUI(
        navController = rememberNavController(),
        currentUser = UserDto(),
        addressDto = AddressDto(id = null, name = "test", code = "loc_test"),
        isDashboard = true,
        status = "PREGNANT"
    )
}

@Composable
fun ResidencesUI(
    navController: NavController,
    currentUser: UserDto,
    addressDto: AddressDto?,
    dateOfCheckup: String? = null,
    isArchive: Boolean = false,
    isCompleted: Boolean? = null,
    isDashboard: Boolean,
    status: String,
) {
    val residenceViewModel: ResidenceViewModel = hiltViewModel()
    val userViewModel: UserViewModel = hiltViewModel()
    var debouncedQuery by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }
    LaunchedEffect(searchQuery) {
        delay(500L)
        debouncedQuery = searchQuery
    }
    val residences = when {
        status == CheckupStatus.PREGNANT.name -> {
            residenceViewModel.fetchAllUsersByCheckup(
                userId = currentUser.id.toObjectId(),
                isSuperAdmin = currentUser.isSuperAdmin,
                checkup = 1,
                isArchive = isArchive,
            )
        }
        dateOfCheckup != null -> {
            residenceViewModel.fetchUsersByCheckup(
                userId = currentUser.id.toObjectId(),
                isSuperAdmin = currentUser.isSuperAdmin,
                dateOfCheckup = dateOfCheckup
            )
        }
        status == CheckupStatus.NORMAL.name -> {
            residenceViewModel.fetchAllUsersWithNormalCondition(
                userId = currentUser.id.toObjectId(),
                isSuperAdmin = currentUser.isSuperAdmin,
                isNormal = true,
                isArchive = isArchive
            )
        }
        status == CheckupStatus.CRITICAL.name -> {
            residenceViewModel.fetchAllUsersWithCriticalCondition(
                userId = currentUser.id.toObjectId(),
                isSuperAdmin = currentUser.isSuperAdmin,
                isCritical = true,
                isArchive = isArchive
            )
        }
        else -> {
            residenceViewModel.fetchUsers(
                userId = currentUser.id.toObjectId(),
                isSuperAdmin = currentUser.isSuperAdmin,
                addressName = addressDto?.name,
                isArchive = isArchive,
                isCompleted = if(isDashboard) isCompleted else null
            )
        }
    }
    val filteredResidences = residences.filter {
        it.firstName.contains(debouncedQuery, ignoreCase = true) ||
        it.middleName?.contains(debouncedQuery, ignoreCase = true) ?: false ||
        it.lastName.contains(debouncedQuery, ignoreCase = true)
    }
    val pregnantCount = if (status == CheckupStatus.PREGNANT.name) {
        residences.count { user ->
            val checkup = residenceViewModel.getCheckupForUser(user.id!!)
            checkup?.checkup!! >= 1
        }
    } else {
        0
    }
    val fetchResidencesReportDetails = userViewModel.fetchResidencesReportDetails(addressName = addressDto?.name!!)
    val isShowFloatingIcon = rememberSaveable { mutableStateOf( !isArchive)}
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            floatingActionButton = {
                if (isShowFloatingIcon.value && addressDto != null) {
                    FloatingIcon(
                        navController,
                        addressDto,
                        currentUser,
                        filteredResidences = fetchResidencesReportDetails,
                        onExportToPDF = { data ->
                            exportToPDF(
                                context = context,
                                data = data,
                                onFinish = { file ->
                                    openFile(context, file)
                                },
                                onError = { e ->
                                    Toast.makeText(
                                        context,
                                        "Error creating PDF: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }
                    )
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFFFFF))
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (status == CheckupStatus.PREGNANT.name) {
                    Box(
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "Total of Pregnant: $pregnantCount",
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontFamily = FontFamily.SansSerif
                            )
                        }
                    }
                }else{
                    Spacer(modifier = Modifier.height(40.dp))
                }
                SearchIcon(
                    searchQuery = searchQuery,
                    onSearchQueryChanged = { searchQuery = it },
                )
                Spacer(modifier = Modifier.padding(bottom = 3.dp))
                LazyColumn(
                    modifier = Modifier
                        .background(Color.White)
                        .height(600.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(filteredResidences) { residence ->
                        SingleItemCard(userDto = residence, navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun UsersImageContainer(imageBase64: String? = null) {
    Box(
        Modifier
            .height(55.dp)
    ){
       Box(
           modifier = Modifier
               .size(51.dp)
               .clip(CircleShape)
               .background(Color(0xFF6650a4))
               .border(1.dp, Color(0xFF6650a4), CircleShape),
           contentAlignment = Alignment.Center
       ) {
           if (imageBase64 != null && imageBase64.isNotBlank()) {
               val resizedBitmap = remember(imageBase64) {
                   decodeBase64ToBitmap(imageBase64)?.let { bitmap ->
                       resizeBitmap(bitmap, maxWidth = 500, maxHeight = 500)
                   }
               }

               if (resizedBitmap != null) {
                   Image(
                       bitmap = resizedBitmap.asImageBitmap(),
                       contentDescription = null,
                       contentScale = ContentScale.Crop,
                       modifier = Modifier
                           .size(51.dp)
                           .clip(CircleShape)
                   )
               } else {
                   PlaceholderImage()
               }
           } else {
               PlaceholderImage()
           }
       }
    }
}

@Composable
fun SingleItemCard(
    userDto: UserDto,
    navController: NavController
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 5.dp)
            .height(60.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate(MainNav.HealthRecord(userDto.id!!)) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(10.dp))
            UsersImageContainer(userDto.imageBase64)
            Text(
                text = "${userDto.firstName} ${userDto.middleName} ${userDto.lastName}",
                fontSize = 17.sp,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier
                    .padding(start = 8.dp, bottom = 4.dp)
                    .weight(1f),
                color = Color.Black
            )
        }
        HorizontalDivider(
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth(),
            thickness = 1.dp,
            color = Color(0xFF6650a4)
        )
    }
}

@Composable
fun SearchIcon(
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
            Text(text = "Search...", color = Color.Black)
        }
    )
}

@Composable
fun FloatingIcon(
    navController: NavController,
    addressDto: AddressDto,
    currentUser: UserDto,
    filteredResidences: List<UserReport>,
    onExportToPDF: (List<UserReport>) -> Unit
) {
    val context = LocalContext.current
    val activity = context as? MainActivity
    Column(
        modifier = Modifier
            .background(Color.Transparent),
        horizontalAlignment = Alignment.End
    ) {
        if (currentUser.isSuperAdmin) {
            FloatingActionButton(
                onClick = {
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P && activity != null) {
                        activity.requestStoragePermission()
                    }
                    onExportToPDF(filteredResidences)
                },
                containerColor = Color(0xFF6650a4),
                contentColor = Color(0xFFFFFFFF),
                shape = CircleShape,
                modifier = Modifier
                    .size(75.dp)
                    .offset(x = (-5).dp, y = (-7).dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.printer),
                    contentDescription = "Export",
                    modifier = Modifier.size(30.dp),
                    tint = Color.White
                )
            }
        } else {
            FloatingActionButton(
                onClick = { navController.navigate(MainNav.CreateUser(addressDto.id)) },
                containerColor = Color(0xFF6650a4),
                contentColor = Color(0xFFFFFFFF),
                shape = CircleShape,
                modifier = Modifier
                    .size(75.dp)
                    .offset(x = (-5).dp, y = (-7).dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add",
                    modifier = Modifier
                        .size(30.dp)
                )
            }
        }
    }
}