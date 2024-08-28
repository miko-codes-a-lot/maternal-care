package org.maternalcare.modules.main.user.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import org.maternalcare.R
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.viewmodel.UserViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UsersUI(navController: NavController) {
    val userViewModel: UserViewModel = hiltViewModel()

    val users by produceState<List<UserDto>>(emptyList(), userViewModel) {
        value = userViewModel.fetchUsers()
    }
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
    ) {
        Scaffold(
            floatingActionButton = {
                FloatParentFloatingIcon(navController)
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.padding( top = 45.dp) )
                UsersSearchIcon(navController)
                LazyColumn {
                    items(users) { user ->
                        UserSingleLine(user.email, navController = navController)
                    }

                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersSearchIcon(navController: NavController) {
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
            IconButton(onClick = {navController.navigate(MainNav.Menu)}) {
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

@Composable
fun UserSingleLine(sampleEmailValue: String,navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
//                .clickable {
//                    navController.navigate(MainNav.ChooseCheckup) // Preview
//                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.adminicon),
                contentDescription = "Person Icon",
                tint = Color(0xFF6650a4),
                modifier = Modifier.size(26.dp)
            )
            Text(
                text = sampleEmailValue,
                fontSize = 18.sp,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            )
        }
        Divider(
            color = Color(0xFF6650a4),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FloatParentFloatingIcon(navController: NavController) {
    var expanded by remember { mutableStateOf(false) }

    val items = listOf(
        painterResource(id = R.drawable.editicon),
        painterResource(id = R.drawable.delete)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(horizontalAlignment = Alignment.End) {
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it }) + expandVertically(),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it }) + shrinkVertically()
            ) {
                LazyColumn(
                    modifier = Modifier.offset(y = 4.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    items(items.size) { index ->
                        when (index) {
                            0 -> FloatChildIcon(
                                painter = items[index],
                                navController = navController
                            ) {
                                navController.navigate(MainNav.CreateUser)
                            }
                            1 -> FloatChildIcon(
                                painter = items[index],
                                navController = navController
                            ) {
                            // Navigate controller for deletion
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
            FloatingActionButton(
                onClick = { expanded = !expanded },
                containerColor = Color(0xFF6650a4),
                contentColor = Color(0xFFFFFFFF),
                shape = CircleShape,
                modifier = Modifier
                    .size(75.dp)
                    .offset(x = (1).dp, y = (7).dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add",
                    modifier = Modifier
                        .rotate(if (expanded) 45f else 0f)
                        .size(30.dp)
                )
            }
        }
    }
}

@Composable
fun FloatChildIcon(painter: Painter, navController: NavController, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 11.dp)
    ) {
        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier.size(52.dp),
            containerColor = Color(0xFF6650a4),
            contentColor = Color(0xFFFFFFFF)
        ) {
            Icon(
                painter = painter,
                contentDescription = "childIcon",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}