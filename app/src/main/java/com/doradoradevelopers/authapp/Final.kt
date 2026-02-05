package com.doradoradevelopers.authapp

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun Greet(user: String, modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val db = Firebase.firestore

    val firstName = remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(true) }

    androidx.compose.runtime.LaunchedEffect(user) {
        db.collection("Users")
            .document(user) // user = UID ✅
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    firstName.value = document.getString("firstName") ?: ""
                } else {
                    Toast.makeText(context, "User data not found", Toast.LENGTH_SHORT).show()
                }
                isLoading.value = false
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to fetch user", e)
                Toast.makeText(context, "Failed to load user data", Toast.LENGTH_SHORT).show()
                isLoading.value = false
            }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Welcome 🎉")

        Spacer(modifier = Modifier.padding(20.dp))

        if (isLoading.value) {
            Text(text = "Loading...")
        } else {
            Text(text = "Hello ${firstName.value}")
        }
    }
}
