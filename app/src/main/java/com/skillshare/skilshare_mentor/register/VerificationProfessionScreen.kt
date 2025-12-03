package com.skillshare.skilshare_mentor.register

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.skillshare.skilshare_mentor.ui.theme.*

enum class VerificationScreen {
    METHOD_SELECTION,
    UNIVERSITY_EMAIL,
    VALIDATION_SUCCESS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificationProfessionScreen(
    viewModel: RegisterViewModel,
    onBackClick: () -> Unit,
    onEmailVerified: () -> Unit,
    onDocumentSelected: () -> Unit
) {
    var currentScreen by remember { mutableStateOf(VerificationScreen.METHOD_SELECTION) }

    when (currentScreen) {
        VerificationScreen.METHOD_SELECTION -> {
            MethodSelectionScreen(
                onBackClick = onBackClick,
                onContinueClick = {
                    currentScreen = VerificationScreen.UNIVERSITY_EMAIL
                },
                onDocumentSelected = onDocumentSelected
            )
        }
        VerificationScreen.UNIVERSITY_EMAIL -> {
            UniversityEmailScreen(
                viewModel = viewModel,
                onBackClick = { currentScreen = VerificationScreen.METHOD_SELECTION },
                onContinueClick = {
                    currentScreen = VerificationScreen.VALIDATION_SUCCESS
                }
            )
        }
        VerificationScreen.VALIDATION_SUCCESS -> {
            ValidationSuccessScreen(
                onBackClick = { currentScreen = VerificationScreen.UNIVERSITY_EMAIL },
                onContinueClick = onEmailVerified
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MethodSelectionScreen(
    onContinueClick: () -> Unit,
    onBackClick: () -> Unit,
    onDocumentSelected: () -> Unit
) {
    var selectedMethod by remember { mutableStateOf<VerificationMethod?>(null) }

    LaunchedEffect(selectedMethod) {
        if (selectedMethod == VerificationMethod.DOCUMENT) {
            onDocumentSelected()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBackIosNew, "Back", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Surface(modifier = Modifier.fillMaxSize().padding(paddingValues), color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp)) {
                // ... (Imagen y Título igual) ...
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("file:///android_asset/images/common/foxdungee/verification_method.png")
                        .build()
                )
                Image(
                    painter = painter,
                    contentDescription = "Verification Method",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(200.dp, 200.dp).align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Verification of profession", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground, textAlign = TextAlign.Center, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(32.dp))

                VerificationMethodCard(
                    method = VerificationMethod.GMAIL,
                    isSelected = selectedMethod == VerificationMethod.GMAIL,
                    onClick = { selectedMethod = VerificationMethod.GMAIL }
                )
                Spacer(modifier = Modifier.height(16.dp))
                VerificationMethodCard(
                    method = VerificationMethod.DOCUMENT,
                    isSelected = selectedMethod == VerificationMethod.DOCUMENT,
                    onClick = { selectedMethod = VerificationMethod.DOCUMENT }
                )

                Spacer(modifier = Modifier.weight(1f))

                if (selectedMethod == VerificationMethod.GMAIL) {
                    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 40.dp), horizontalArrangement = Arrangement.End) {
                        TextButton(
                            onClick = onContinueClick,
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.tertiary),
                            enabled = selectedMethod != null
                        ) {
                            Text("Continue", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.ArrowForward, "Continue", modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UniversityEmailScreen(
    viewModel: RegisterViewModel,
    onContinueClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val isValidEmail by remember(viewModel.email) {
        derivedStateOf {
            viewModel.email.endsWith("@university.edu.com") && viewModel.email.length > "@university.edu.com".length
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBackIosNew, "Back", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Surface(modifier = Modifier.fillMaxSize().padding(paddingValues), color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp)) {
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("file:///android_asset/images/common/foxdungee/university_email.png")
                        .build()
                )
                Image(painter = painter, contentDescription = "University Email", contentScale = ContentScale.Fit, modifier = Modifier.size(200.dp, 200.dp).align(Alignment.CenterHorizontally))
                Spacer(modifier = Modifier.height(16.dp))
                Text("University email", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground, textAlign = TextAlign.Center, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                Text("Enter your university email to validate your teaching", style = MaterialTheme.typography.bodyMedium, color = Gray, textAlign = TextAlign.Center, fontSize = 14.sp, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(32.dp))
                Text("Email", style = MaterialTheme.typography.bodyMedium, color = Gray, fontSize = 14.sp, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp))

                EmailTextFieldWithValidation(
                    value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    placeholder = "Enter your university email",
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    isValid = isValidEmail
                )

                if (viewModel.email.isNotEmpty() && !isValidEmail) {
                    Text("Please use a valid @university.edu.com email", color = RedError, fontSize = 12.sp, modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp))
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 40.dp), horizontalArrangement = Arrangement.End) {
                    TextButton(
                        onClick = onContinueClick,
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.tertiary),
                        enabled = isValidEmail
                    ) {
                        Text("Continue", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Default.ArrowForward, "Continue", modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidationSuccessScreen(
    onContinueClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBackIosNew, "Back", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Surface(modifier = Modifier.fillMaxSize().padding(paddingValues), color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Icon(Icons.Default.CheckCircle, "Success", tint = GreenSuccess, modifier = Modifier.size(120.dp))
                Spacer(modifier = Modifier.height(24.dp))
                Text("Validation successful", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground, textAlign = TextAlign.Center, fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
                Text("Your university email has been successfully verified. You can now continue with the registration process.", style = MaterialTheme.typography.bodyMedium, color = Gray, textAlign = TextAlign.Center, fontSize = 16.sp, lineHeight = 24.sp, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(40.dp))
                TextButton(
                    onClick = onContinueClick,
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.tertiary)
                ) {
                    Text("Continue", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.ArrowForward, "Continue", modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

sealed class VerificationMethod(val title: String, val description: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object GMAIL : VerificationMethod("Gmail", "Verify using your university Gmail account", Icons.Default.Email)
    object DOCUMENT : VerificationMethod("Attach document", "Upload a document proving your profession", Icons.Default.UploadFile)
}

@Composable
fun VerificationMethodCard(method: VerificationMethod, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().height(80.dp),
        shape = RoundedCornerShape(12.dp),
        color = White,
        border = BorderStroke(2.dp, if (isSelected) PrimaryColor else BorderGray),
        onClick = onClick
    ) {
        Row(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(method.icon, method.title, tint = if (isSelected) PrimaryColor else Gray, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(method.title, style = MaterialTheme.typography.bodyMedium, color = PrimaryColor, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text(method.description, style = MaterialTheme.typography.bodySmall, color = Gray, fontSize = 12.sp)
            }
            if (isSelected) {
                Icon(Icons.Default.CheckCircle, "Selected", tint = GreenSuccess, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun EmailTextFieldWithValidation(value: String, onValueChange: (String) -> Unit, placeholder: String, modifier: Modifier = Modifier, isValid: Boolean = false) {
    var isFocused by remember { mutableStateOf(false) }
    Box(
        modifier = modifier.height(56.dp).clip(RoundedCornerShape(12.dp)).background(White)
            .border(1.dp, when { isValid -> GreenSuccess; isFocused -> PrimaryColor; else -> BorderGray }, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Icon(Icons.Default.Email, null, tint = if (isFocused) PrimaryColor else Gray, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f).onFocusChanged { isFocused = it.isFocused },
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = PrimaryColor, fontSize = 16.sp),
                cursorBrush = SolidColor(PrimaryColor),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box { if (value.isEmpty()) Text(placeholder, color = Gray, fontSize = 16.sp); innerTextField() }
                }
            )
            if (isValid) Icon(Icons.Default.CheckCircle, "Valid", tint = GreenSuccess, modifier = Modifier.size(20.dp))
        }
    }
}