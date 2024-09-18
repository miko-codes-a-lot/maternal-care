package org.maternalcare.modules.main.residence.ui

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import org.maternalcare.R
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.residence.enum.CheckupStatus
import org.maternalcare.modules.main.residence.viewmodel.ResidenceViewModel
import org.maternalcare.modules.main.user.model.dto.AddressDto
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.shared.ext.toObjectId

@Preview(showSystemUi = true)
@Composable
fun ResidencesPrev() {
    ResidencesUI(
        navController = rememberNavController(),
        currentUser = UserDto(),
        addressDto = AddressDto(id = null, name = "test", code = "loc_test")
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ResidencesUI(navController: NavController, currentUser: UserDto, addressDto: AddressDto) {
    val residenceViewModel: ResidenceViewModel = hiltViewModel()
    val residences = residenceViewModel.fetchUsers(
        userId = currentUser.id.toObjectId(),
        isSuperAdmin = currentUser.isSuperAdmin,
        addressName = addressDto.name,
    )

    val isShowFloatingIcon = rememberSaveable { mutableStateOf( !currentUser.isSuperAdmin)}

    Scaffold(
        floatingActionButton = {
            if(isShowFloatingIcon.value) {
                FloatingIcon(navController, addressDto)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            SearchIcon(navController)
            Spacer(modifier = Modifier.padding(bottom = 3.dp))
            LazyColumn(
                modifier = Modifier
                    .background(Color.White)
                    .height(600.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(residences) { residence ->
                    SingleItemCard(userDto = residence, navController = navController)
                }
            }
        }
    }
}

@Composable
fun UsersImageContainer(imageUri: Uri? = null) {
    Box(
        Modifier
            .height(45.dp)
    ){
       Box(
           modifier = Modifier
               .size(40.dp)
               .clip(CircleShape)
               .background(Color(0xFF6650a4))
               .border(3.dp, Color(0xFF6650a4), CircleShape),
           contentAlignment = Alignment.Center
       ) {
           if (imageUri != null) {
               AsyncImage(
                   model = imageUri,
                   contentDescription = null,
                   contentScale = ContentScale.Crop,
                   modifier = Modifier
                       .size(35.dp)
                       .clip(CircleShape)
               )
           } else {
               Icon(
                   painter = painterResource(id = R.drawable.person),
                   contentDescription = "Default placeholder",
                   modifier = Modifier.size(30.dp),
                   tint = Color.White
               )
           }
       }
    }
}

@Composable
fun SingleItemCard(userDto: UserDto, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate(MainNav.ChooseCheckup(userDto.id!!)) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(10.dp))
            UsersImageContainer()
            Text(
                text = "${userDto.firstName} ${userDto.lastName}",
                fontSize = 18.sp,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f),
                color = Color.Black
            )
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color(0xFF6650a4)
        )
    }
}

@Composable
fun SearchIcon(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = searchQuery,
        onValueChange = { searchQuery = it },
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
            IconButton(onClick = {navController.navigate(MainNav.Addresses(CheckupStatus.ALL.name))}) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close Icon",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Black,
            focusedBorderColor = Color.Black,
            disabledBorderColor = Color.Gray,
            errorBorderColor = Color.Red,
            cursorColor = Color.Black
        ),
        placeholder = {
            Text(text = "Search...", color = Color.Gray)
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FloatingIcon(navController: NavController, addressDto: AddressDto) {
    Column(
        modifier = Modifier
            .background(Color.Transparent),
        horizontalAlignment = Alignment.End
    ) {
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

