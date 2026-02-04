package com.doradoradevelopers.authapp

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
fun RegisterScreen(navController: NavController) {

    val context = LocalContext.current
    val email = remember { mutableStateOf(TextFieldValue()) }
    val password = remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier.fillMaxSize().padding(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(value = email.value, onValueChange = { email.value = it })
        Spacer(modifier = Modifier.padding(top = 50.dp))
        TextField(value = password.value, onValueChange = { password.value = it })

        Spacer(modifier = Modifier.padding(top = 20.dp))

        Button(
            onClick = { register(email.value.text,password.value.text,context ) },
            modifier = Modifier
        ) {
            Text(text = "Register")

        }
    }


}

fun register(email: String, password: String,context: Context) {
    lateinit var auth: FirebaseAuth

    auth = Firebase.auth

    auth.createUserWithEmailAndPassword(email, password)
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
                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                Toast.makeText(
                    context,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT,
                ).show()
//                updateUI(null)
            }
        }
}


@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(navController = NavController(LocalContext.current))
}