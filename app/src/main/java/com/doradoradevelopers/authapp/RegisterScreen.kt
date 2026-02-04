package com.doradoradevelopers.authapp

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

@Composable
fun RegisterScreen(navController: NavController) {

    val firstName = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val phoneNumber = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = firstName.value,
            onValueChange = { firstName.value = it },
            label = { Text("First Name") },
            placeholder = { Text("Enter first name") }
        )

        Spacer(modifier = Modifier.padding(top = 20.dp))

        TextField(
            value = lastName.value,
            onValueChange = { lastName.value = it },
            label = { Text("Last Name") },
            placeholder = { Text("Enter last name") }
        )

        Spacer(modifier = Modifier.padding(top = 20.dp))

        TextField(
            value = phoneNumber.value,
            onValueChange = { phoneNumber.value = it },
            label = { Text("Phone Number") },
            placeholder = { Text("10-digit mobile number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.padding(top = 20.dp))

        TextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") },
            placeholder = { Text("example@gmail.com") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.padding(top = 20.dp))

        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            placeholder = { Text("Minimum 6 characters") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.padding(top = 20.dp))


        Button(
            onClick = {
                if (
                    firstName.value.isBlank() ||
                    lastName.value.isBlank() ||
                    phoneNumber.value.isBlank() ||
                    email.value.isBlank() ||
                    password.value.length < 6
                ) {
                    Toast.makeText(context, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
                } else {
                    register(
                        email = email.value,
                        password = password.value,
                        context = context,
                        phone = phoneNumber.value,
                        firstName = firstName.value,
                        lastName = lastName.value,
                        navController = navController
                    )
                }
            }
        ) {
            Text("Register")
        }

    }


}


fun register(
    email: String,
    password: String,
    context: Context,
    phone: String,
    firstName: String,
    lastName: String,
    navController: NavController
) {
    val auth = Firebase.auth
    val db = Firebase.firestore

    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {

                val uid = auth.currentUser!!.uid

                val userMap = hashMapOf(
                    "uid" to uid,
                    "firstName" to firstName,
                    "lastName" to lastName,
                    "phone" to phone,
                    "email" to email,
                    "createdAt" to Timestamp.now()
                )


                db.collection("Users")
                    .document(uid)
                    .set(userMap)
                    .addOnSuccessListener {

                        Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()

                        navController.navigate("homeScreen/$uid") {
                            popUpTo("registerScreen") { inclusive = true }
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Failed to save user data", Toast.LENGTH_SHORT).show()
                    }

            } else {
                Toast.makeText(
                    context,
                    task.exception?.message ?: "Registration failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
}



@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(navController = NavController(LocalContext.current))
}