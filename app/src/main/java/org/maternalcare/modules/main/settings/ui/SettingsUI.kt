package org.maternalcare.modules.main.settings.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import org.maternalcare.modules.intro.IntroNav
import org.maternalcare.modules.main.MainNav

@Preview(showSystemUi = true)
@Composable
fun SettingsUIPreview() {
    SettingsUI(navController = rememberNavController())
}

@Composable
fun SettingsUI(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Profile(navController)

            TextButton(onClick = { navController.navigate(IntroNav.Login) }) {
                Text(text = "Logout",
                    fontSize = 15.sp,
                    color = Color(0xFF6650a4),
                    fontFamily = FontFamily.SansSerif,
                    modifier = Modifier
                        .padding(10.dp)
                )
            }

            Spacer(modifier = Modifier.padding(vertical = 25.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(navController: NavController){
    var picUrl by remember { mutableStateOf("") }
    val saveImageUri: (Uri) -> Unit = { uri -> picUrl = uri.toString() }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImagePicker(selectedImageUri = Uri.parse(picUrl), onImageSelected = saveImageUri)

        Spacer(modifier = Modifier.padding(vertical = 15.dp))

        UserDetails()

        Spacer(modifier = Modifier.padding(vertical = 5.dp))

        Setting(navController)
    }
}

@Composable
fun UserDetails() {
    val userDetails = listOf( "Juan", "R.", "Dela Cruz" )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        userDetails.forEach { fullName ->
            Text(
                text = fullName,
                fontSize = 17.sp,
                fontFamily = FontFamily.SansSerif,
                color = Color(0xFF6650a4),
                modifier = Modifier.padding(horizontal = 3.dp)
            )
        }
    }
}

@Composable
fun ImagePicker(selectedImageUri: Uri? = null, onImageSelected: (Uri) -> Unit = {}) {
    var selectedImgUri by remember { mutableStateOf<Uri?>(null) }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                selectedImgUri = it
                onImageSelected(it)
            }
        }
    )

    Box(
        Modifier.height(140.dp)
    ) {
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .background(Color(0xFF6650a4))
                .border(3.dp, Color(0xFF6650a4), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (selectedImgUri != null) {
                AsyncImage(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(140.dp)
                        .clip(CircleShape),
                    model = selectedImgUri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.person),
                    contentDescription = "Default placeholder",
                    modifier = Modifier
                        .size(90.dp),
                    tint = Color.White,

                )
            }
        }
        IconButton(
            onClick = {
                photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            modifier = Modifier
                .size(35.dp)
                .padding(2.dp)
                .clip(CircleShape)
                .align(Alignment.BottomEnd),
            colors = IconButtonDefaults.iconButtonColors(Color(0xFF6650a4)),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.cameraalt),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp),
                tint = Color.White
            )
        }
    }
}


@Composable
fun Setting(navController: NavController){
    val settingsMenu = listOf(
        SettingItem(text = "Email"){
            navController.navigate("${MainNav.EditSettings}/email")
        },
        SettingItem(text = "Password"){
            navController.navigate("${MainNav.EditSettings}/password")
        }
    )
    Column {
        settingsMenu.forEach { menuSettings ->
            Spacer(modifier = Modifier.height(10.dp))
            SettingButton(text = menuSettings.text, onClick = menuSettings.action)
        }
    }
}

@Composable
fun SettingButton(text: String, onClick: () -> Unit){
    Button(onClick = onClick,
        modifier = Modifier
            .width(290.dp)
            .height(70.dp)
            .padding(5.dp)
            .border(2.dp, color = Color(0xFF6650a4), shape = RoundedCornerShape(5.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color(0xFF6650a4)
        )
    ) {
        Row(
            modifier = Modifier
        ){
            Text(text = text, fontSize = 16.sp, fontFamily = FontFamily.SansSerif)
            Spacer(modifier = Modifier.weight(0.1f))
            Icon(painter = painterResource(id = R.drawable.editicon)
                ,contentDescription = null,
                modifier = Modifier.size(20.dp))
        }
    }
}
data class SettingItem(val text: String, val action: () -> Unit)