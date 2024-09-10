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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import org.maternalcare.R
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.residence.enum.CheckupStatus

@Preview(showSystemUi = true)
@Composable
fun ResidencePreview() {
    ResidencesUI(navController = rememberNavController())
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ResidencesUI(navController: NavController) {
    Scaffold(
        floatingActionButton = {
            FloatingIcon(navController)
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
                items(residencesList) { residence ->
                    SingleItemCard(residenceName = residence,navController = navController)
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

val residencesList = listOf(
    "Robert Downey Jr.", "Scarlett Johansson", "Chris Evans",
    "Mark Ruffalo", "Chris Hemsworth", "Tom Holland",
    "Benedict Cumberbatch", "Chadwick Boseman", "Chris Pratt",
    "Benedict Cumberbatch", "Chadwick Boseman", "Chris Pratt",
    "Benedict Cumberbatch", "Chadwick Boseman", "Chris Pratt",
    "Benedict Cumberbatch", "Chadwick Boseman", "Chris Pratt",
    "Benedict Cumberbatch", "Chadwick Boseman", "Chris Pratt",
    "Benedict Cumberbatch", "Chadwick" +
            " Boseman", "Chris Pratt",
)
@Composable
fun SingleItemCard(residenceName: String,navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate(MainNav.ChooseCheckup) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(10.dp))
            UsersImageContainer()
            Text(
                text = residenceName,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchIcon(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = searchQuery,
        onValueChange = { searchQuery = it },
        leadingIcon = {
            IconButton(onClick = { navController.navigate(MainNav.ChooseCheckup)} ) {
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
fun FloatingIcon(navController: NavController) {
    Column(
        modifier = Modifier
            .background(Color.Transparent),
        horizontalAlignment = Alignment.End
    ) {
        FloatingActionButton(
            onClick = { navController.navigate(MainNav.CreateUser) },
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

