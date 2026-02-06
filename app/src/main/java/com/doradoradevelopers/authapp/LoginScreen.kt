package com.doradoradevelopers.authapp

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


@Composable
fun LoginScreen(navController: NavController) {

    var showPhonePopup by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        // 🔹 Login Screen (background)
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
                    .size(300.dp)
            )

            Box(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .background(Color.White)
            ) {

                Button(
                    onClick = { showPhonePopup = true },
                    modifier = Modifier
                        .padding(top = 40.dp, start = 16.dp, end = 16.dp)
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue,
                        contentColor = Color.White
                    )
                ) {
                    Text("Login")
                }

                TermsAndPrivacyRow(
                    onTermsClick = { },
                    onPrivacyClick = { }
                )


                BottomOptionsCard(
                    modifier = Modifier
                        .padding(top = 120.dp)
                        .align(Alignment.TopCenter)
                )

                Text(
                    text = "App Version 1.0.1",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 20.dp)
                )
            }
        }

        // 🔹 Dim background
        if (showPhonePopup) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable { showPhonePopup = false }
            )
        }

        // 🔹 Bottom popup card
        if (showPhonePopup) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                PhonePopupUI(
                    onClose = { showPhonePopup = false }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhonePopupUI(onClose: () -> Unit) {

    val countryCodes = listOf("+1", "+44", "+91", "+61")
    var expanded by remember { mutableStateOf(false) }
    var selectedCode by remember { mutableStateOf(countryCodes[2]) }
    var phoneNumber by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 320.dp)
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {

        // 🔹 Drag indicator
        Box(
            modifier = Modifier
                .size(width = 40.dp, height = 4.dp)
                .background(Color.LightGray, RoundedCornerShape(50))
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Enter Phone Number",
            fontSize = 18.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.weight(0.3f)
            ) {
                TextField(
                    value = selectedCode,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.menuAnchor(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = textFieldCustomColors()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    countryCodes.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                selectedCode = it
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            TextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                placeholder = { Text("Phone Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                modifier = Modifier.weight(0.7f),
                shape = RoundedCornerShape(10.dp),
                colors = textFieldCustomColors()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // Continue logic later
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            )
        ) {
            Text("Continue")
        }
    }
}

@Composable
fun textFieldCustomColors() = TextFieldDefaults.colors(
    focusedTextColor = Color.Black,
    unfocusedTextColor = Color.Black,
    cursorColor = Color.Black,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    focusedContainerColor = Color(0xFFF2F2F2), // Light gray background to make it visible
    unfocusedContainerColor = Color(0xFFF2F2F2)
)

//fun login(email : String, password : String,context: Context) {
//    val auth: FirebaseAuth = Firebase.auth
//
//
//    auth.signInWithEmailAndPassword(email, password)
//        .addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                // Sign in success, update UI with the signed-in user's information
//                Toast.makeText(
//                    context,
//                    "Authentication success.",
//                    Toast.LENGTH_SHORT,
//                ).show()
//                val user = auth.currentUser
////              updateUI(user)
//            } else {
//                // If sign in fails, display a message to the user.
//                Log.w(TAG, "signInWithEmail:failure")
//                Toast.makeText(
//                    context,
//                    "Authentication failed.",
//                    Toast.LENGTH_SHORT,
//                ).show()
//            }
//        }
//}


@Composable
fun BottomOptionsCard(modifier: Modifier) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 70.dp)
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
    }
}

@Composable
fun TermsAndPrivacyRow(
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit
) {
    val annotatedText = buildAnnotatedString {

        append("By tapping in, I accept the ")

        pushStringAnnotation(
            tag = "TERMS",
            annotation = "terms"
        )
        withStyle(
            style = SpanStyle(
                color = Color.Gray,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append("terms of service")
        }
        pop()

        append(" & ")

        pushStringAnnotation(
            tag = "PRIVACY",
            annotation = "privacy"
        )
        withStyle(
            style = SpanStyle(
                color = Color.Gray,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append("privacy policy")
        }
        pop()
    }

    ClickableText(
        text = annotatedText,
        style = TextStyle(
            fontSize = 13.sp,
            color = Color.Black,
            textAlign = TextAlign.Center
        ),
        modifier = Modifier
            .padding(top = 115.dp)
            .fillMaxWidth()
            .padding(horizontal = 25.dp),
        onClick = { offset ->
            annotatedText.getStringAnnotations(offset, offset)
                .firstOrNull()?.let { annotation ->
                    when (annotation.tag) {
                        "TERMS" -> onTermsClick()
                        "PRIVACY" -> onPrivacyClick()
                    }
                }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(rememberNavController())
}