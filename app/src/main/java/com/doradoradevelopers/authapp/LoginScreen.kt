package com.doradoradevelopers.authapp

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


@Composable
fun LoginScreen(navController: NavController) {

    val context = LocalContext.current
    val username = remember { mutableStateOf(TextFieldValue()) }
    val password = remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier
            .background(Color.Blue)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.mipmap.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 100.dp)
                .size(350.dp)
        )

        Box(
            modifier = Modifier
                .padding(top = 50.dp)
                .fillMaxSize()
                .heightIn(min = 350.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 30.dp,
                        topEnd = 30.dp
                    )
                )
                .background(Color.White)
        ) {
            Button(
                onClick = {
                    navController.navigate("registerScreen")
                },
                modifier = Modifier
                    .padding(top = 40.dp, start = 15.dp, end = 15.dp)
                    .fillMaxWidth()
                    .size(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp),
            ) {
                Text(text = "Login")
            }

            Row(
                modifier = Modifier.padding(top = 110.dp, start = 18.dp)
            ) {
                Text(
                    text = "By tapping in, I accept the ",
                    fontSize = 14.sp,
                )

                Text(
                    text = "terms of service",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        // open terms
                    }
                )

                Text(
                    text = " & ",
                    fontSize = 14.sp,
                )

                Text(
                    text = "privacy policy",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        // open privacy
                    }
                )
            }


            BottomOptionsCard(
                modifier = Modifier
                    .padding(top = 160.dp)
                    .align(Alignment.TopCenter)
            )





            Text(
                text = "App Version 1.0.1",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .align(Alignment.BottomCenter)
            )


        }
    }
}

fun login(email : String, password : String,context: Context) {
    val auth: FirebaseAuth = Firebase.auth


    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Toast.makeText(
                    context,
                    "Authentication success.",
                    Toast.LENGTH_SHORT,
                ).show()
                val user = auth.currentUser
//              updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithEmail:failure")
                Toast.makeText(
                    context,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
}


@Composable
fun BottomOptionsCard(modifier: Modifier) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .then(modifier),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column {
            SettingsItem(title = "Offers")

            Divider(
                color = Color.LightGray.copy(alpha = 0.6f),
                thickness = 0.7.dp
            )

            SettingsItem(title = "Feedback")
        }
    }
}



@Composable
fun SettingsItem(
    context: Context = LocalContext.current,
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                Toast.makeText(
                    context,
                    "Coming Soon",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = title,
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Arrow",
            tint = Color.Gray
        )
    }
}



@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = NavController(LocalContext.current))
}
