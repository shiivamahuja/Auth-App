package com.doradoradevelopers.authapp

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

private const val TAG = "PhoneAuth"

private var storedVerificationId: String? = null
private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

private var onOtpSentCallback: (() -> Unit)? = null
private var onAuthSuccessCallback: (() -> Unit)? = null
private var onAuthErrorCallback: ((String) -> Unit)? = null


@Composable
fun LoginScreen(navController: NavController) {

    var showPhonePopup by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier.background(Color.Blue).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(R.mipmap.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier.padding(top = 100.dp).size(300.dp)
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
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Login")
                }

                TermsAndPrivacyRow(
                    onTermsClick = {},
                    onPrivacyClick = {},
                    Popupui = false,
                    containerModifier = Modifier.fillMaxWidth().padding(top = 115.dp),
                    textModifier = Modifier.padding(horizontal = 25.dp)
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

        if (showPhonePopup) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable { showPhonePopup = false }
            )

            Box(
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)
            ) {
                PhonePopupUI { showPhonePopup = false }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhonePopupUI(onClose: () -> Unit) {

    val context = LocalContext.current
    val activity = remember { context.findActivity() }

    var isLoading by remember { mutableStateOf(false) }

    val countryCodes = listOf("+1", "+44", "+91", "+61")
    var expanded by remember { mutableStateOf(false) }
    var selectedCode by remember { mutableStateOf("+91") }
    var phoneNumber by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var isOtpSent by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {

        onOtpSentCallback = {
            isLoading = false
            isOtpSent = true
        }

        onAuthSuccessCallback = {
            isLoading = false
            Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
        }

        onAuthErrorCallback = { message ->
            isLoading = false
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {}
            .heightIn(min = 500.dp)
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {

        Text("Login", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Enter phone number to proceed", fontSize = 14.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {

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
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    colors = textFieldCustomColors()
                )

                ExposedDropdownMenu(expanded, { expanded = false }) {
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
                modifier = Modifier.weight(0.7f),
                colors = textFieldCustomColors()
            )
        }

        if (isOtpSent) {
            Spacer(modifier = Modifier.height(12.dp))
            TextField(
                value = otp,
                onValueChange = { otp = it },
                placeholder = { Text("Enter OTP") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldCustomColors()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            onClick = {
                if (!isOtpSent) {
                    if (phoneNumber.length < 10) {
                        Toast.makeText(context, "Enter valid phone number", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isOtpSent = true
                    isLoading = true

                    activity?.let {
                        login("$selectedCode$phoneNumber", it)
                    } ?: run {
                        isLoading = false
                        Toast.makeText(context, "Activity not found", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    if (otp.length < 6) {
                        Toast.makeText(context, "Enter valid OTP", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isLoading = true
                    verifyOtp(otp)
                }
            }
        ) {
            Text(
                when {
                    isLoading && !isOtpSent -> "Sending OTP..."
                    !isOtpSent -> "Send OTP"
                    else -> "Verify OTP"
                }
            )
        }

        TermsAndPrivacyRow(
            onTermsClick = {},
            onPrivacyClick = {},
            Popupui = true,
            containerModifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            textModifier = Modifier.padding(horizontal = 25.dp)
        )
    }
}


fun login(phoneNumber: String, activity: Activity) {

    val auth = FirebaseAuth.getInstance()
    auth.useAppLanguage()

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(auth, credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            onAuthErrorCallback?.invoke(e.localizedMessage ?: "Verification failed")
        }

        override fun onCodeSent(
            id: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            storedVerificationId = id
            resendToken = token
            onOtpSentCallback?.invoke()
        }
    }

    PhoneAuthProvider.verifyPhoneNumber(
        PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
    )
}

fun verifyOtp(otp: String) {
    val id = storedVerificationId ?: run {
        onAuthErrorCallback?.invoke("OTP not sent yet")
        return
    }

    val credential = PhoneAuthProvider.getCredential(id, otp)
    signInWithPhoneAuthCredential(FirebaseAuth.getInstance(), credential)
}

private fun signInWithPhoneAuthCredential(
    auth: FirebaseAuth,
    credential: PhoneAuthCredential
) {
    auth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onAuthSuccessCallback?.invoke()
            } else {
                onAuthErrorCallback?.invoke(
                    task.exception?.localizedMessage ?: "Invalid OTP"
                )
            }
        }
}


fun Context.findActivity(): Activity? {
    var ctx = this
    while (ctx is android.content.ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    return null
}

@Composable
fun textFieldCustomColors() = TextFieldDefaults.colors(
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    focusedContainerColor = Color(0xFFF2F2F2),
    unfocusedContainerColor = Color(0xFFF2F2F2)
)


@Composable
fun BottomOptionsCard(modifier: Modifier) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 70.dp)
            .then(modifier),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            SettingsItem(title = "Offers")
            Divider(color = Color.LightGray.copy(alpha = 0.6f), thickness = 0.7.dp)
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
                Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show()
            }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontSize = 14.sp, color = Color.Black)
    }
}

@Composable
fun TermsAndPrivacyRow(
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    containerModifier: Modifier,
    textModifier: Modifier,
    Popupui: Boolean
) {
    val text = buildAnnotatedString {
        append(if (Popupui) "By clicking, I accept the " else "By tapping in, I accept the ")
        pushStringAnnotation("TERMS", "")
        withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) { append("terms of service") }
        pop()
        append(" & ")
        pushStringAnnotation("PRIVACY", "")
        withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) { append("privacy policy") }
        pop()
    }

    Box(containerModifier, contentAlignment = Alignment.Center) {
        ClickableText(text, modifier = textModifier, style = TextStyle(textAlign = TextAlign.Center)) {}
    }
}

@Preview(showBackground = true)
@Composable
fun PhonePopupUIPreview() {
    PhonePopupUI {}
}
