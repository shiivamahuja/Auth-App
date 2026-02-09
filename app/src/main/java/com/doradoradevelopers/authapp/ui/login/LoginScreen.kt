package com.doradoradevelopers.authapp.ui.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.doradoradevelopers.authapp.R
import com.doradoradevelopers.authapp.utils.findActivity
import com.doradoradevelopers.authapp.navigation.Screen

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel()
) {
    var showPhonePopup by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val closePopup: () -> Unit = {
        showPhonePopup = false
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is LoginEvent.Success -> {
                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                    closePopup()
                    navController.navigate(Screen.Home.createRoute("User")) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }

                is LoginEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    viewModel.resetUiState()
                    closePopup()
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

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
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Login")
                }

                TermsAndPrivacyRow(
                    onTermsClick = { },
                    onPrivacyClick = { },
                    containerModifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 115.dp),
                    textModifier = Modifier.align(Alignment.Center),
                    Popupui = false
                )



                BottomOptionsCard(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 90.dp)
                )

                Text(
                    text = "App Version 1.0.1",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 40.dp)
                )
            }
        }

        if (showPhonePopup) {
            BackHandler { closePopup() }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable { closePopup() }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                PhonePopupUI(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhonePopupUI(viewModel: LoginViewModel) {

    val context = LocalContext.current
    val activity = remember { context.findActivity() }
    val uiState by viewModel.uiState.collectAsState()

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.65f)
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 24.dp)
            .clickable(enabled = false) {}
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
                    value = uiState.countryCode,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.menuAnchor(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                    }
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    listOf("+1", "+44", "+91", "+61").forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                viewModel.updateCountryCode(it)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            TextField(
                value = uiState.phoneNumber,
                onValueChange = { viewModel.updatePhoneNumber(it) },
                placeholder = { Text("Phone Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.weight(0.7f)
            )
        }

        if (uiState.isOtpSent) {
            Spacer(modifier = Modifier.height(12.dp))
            TextField(
                value = uiState.otp,
                onValueChange = { viewModel.updateOtp(it) },
                placeholder = { Text("Enter OTP") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading,
            onClick = {
                if (!uiState.isOtpSent) {
                    if (uiState.phoneNumber.length < 10) {
                        Toast.makeText(context, "Valid number required", Toast.LENGTH_SHORT).show()
                    } else {
                        activity?.let {
                            viewModel.sendOtp(
                                "${uiState.countryCode}${uiState.phoneNumber}",
                                it
                            )
                        }
                    }
                } else {
                    if (uiState.otp.length < 6) {
                        Toast.makeText(context, "Valid OTP required", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.verifyOtp(uiState.otp)
                    }
                }
            }
        ) {
            Text(
                when {
                    uiState.isLoading && !uiState.isOtpSent -> "Sending OTP..."
                    uiState.isLoading && uiState.isOtpSent -> "Verifying..."
                    !uiState.isOtpSent -> "Send OTP"
                    else -> "Verify OTP"
                }
            )
        }

        TermsAndPrivacyRow(
            onTermsClick = { },
            onPrivacyClick = { },
            containerModifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp),
            textModifier = Modifier.align(Alignment.CenterHorizontally),
            Popupui = true
        )
    }
}


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



