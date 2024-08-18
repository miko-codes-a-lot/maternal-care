package org.maternalcare.modules.main.residence.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.maternalcare.R
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.residence.enum.CheckupStatus

@Composable
fun ResidencesUI(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(5.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            SearchIcon(navController)

            LazyColumn {
                items(residencesList) { residence ->
                    SingleItemCard(residenceName = residence,navController = navController)
                }
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
    "Benedict Cumberbatch", "Chadwick Boseman", "Chris Pratt",
)
@Composable
fun SingleItemCard(residenceName: String,navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickable {navController.navigate(MainNav.ChooseCheckup)},
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.person),
                contentDescription = "Person Icon",
                tint = Color(0xFF6650a4),
                modifier = Modifier.size(26.dp)
            )
            Text(
                text = residenceName,
                fontSize = 18.sp,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(start = 8.dp)
                    .weight(1f)
            )
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.edit),
                    contentDescription = "Edit Icon",
                    tint = Color(0xFF6650a4),
                    modifier = Modifier
                        .clickable {}
                        .size(24.dp)
                        .padding(end = 3.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.delete),
                    contentDescription = "Delete Icon",
                    tint = Color(0xFF6650a4),

                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 3.dp)
                        .clickable {}
                )
            }

        }


        Divider(
            color = Color(0xFF6650a4),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
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
        colors = TextFieldDefaults.outlinedTextFieldColors(
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


