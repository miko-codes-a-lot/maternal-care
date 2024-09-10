package org.maternalcare.modules.intro.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.maternalcare.R
import org.maternalcare.modules.intro.IntroNav

@Preview
@Composable
fun SplashUIPreview() {
    SplashUI(navController = rememberNavController())
}

@Composable
fun SplashUI(navController: NavController) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(id = R.drawable.care), contentDescription = "Login Image",
            modifier = Modifier.size(200.dp))

        Text(text = "Maternal Care", fontSize = 45.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Monitoring Application for Pregnant Women", fontSize = 12.sp)
        Spacer(modifier = Modifier.height(70.dp))

        Button(
            onClick = { navController.navigate(IntroNav.Login)},
            modifier = Modifier
                .height(50.dp)
                .width(140.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6650a4)),
            shape = RoundedCornerShape(topStart = 10.dp, bottomEnd = 10.dp),
            enabled = true,
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 30.dp
            )
        ){
            Text(
                text = "Get Started",
                color = Color.White
            )
        }
    }
}
