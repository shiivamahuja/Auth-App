package com.doradoradevelopers.authapp

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

@Composable
fun LoginScreen(navController: NavController) {

    val context = LocalContext.current
    val username = remember { mutableStateOf(TextFieldValue()) }
    val password = remember { mutableStateOf(TextFieldValue()) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 50.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(value = username.value, onValueChange = { username.value = it })
        Spacer(modifier = Modifier.padding(20.dp))
        TextField(value = password.value, onValueChange = { password.value = it })
        Spacer(modifier = Modifier.padding(20.dp))
        Button(onClick = {
            login(username.value.text, password.value.text,context)
        }, modifier = Modifier
            .padding(20.dp)
            .size(width = 150.dp, height = 50.dp)) {
            Text(text = "Login")
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
//                updateUI(user)
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


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = NavController(LocalContext.current))
}
