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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ExpandMore
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.skillshare.skilshare_mentor.ui.theme.*

import android.widget.Toast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalIdentityScreen(
    viewModel: RegisterViewModel,
    onContinueClick: () -> Unit,
    onBackClick: () -> Unit
) {
    // Variables para el Dropdown local
    var expandedGender by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(viewModel.isRegistered) {
        if (viewModel.isRegistered) {
            Toast.makeText(context, "¡Usuario creado en MySQL!", Toast.LENGTH_LONG).show()
            onContinueClick() // Navegamos a AllDone
            viewModel.isRegistered = false
        }
    }

    LaunchedEffect(viewModel.registrationError) {
        viewModel.registrationError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            viewModel.registrationError = null
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
        Surface(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp)) {
                // ... (Imagen y Título) ...
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("file:///android_asset/images/common/foxdungee/personal_identity.png")
                        .build()
                )
                Image(
                    painter = painter,
                    contentDescription = "Personal Identity",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(200.dp).align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Your personal identity",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(32.dp))

                Text("Nickname", style = MaterialTheme.typography.bodyMedium, color = Gray, fontSize = 14.sp, modifier = Modifier.padding(bottom = 8.dp))

                TextFieldWithBorder(
                    value = viewModel.nickname,
                    onValueChange = { viewModel.nickname = it },
                    placeholder = "Enter your nickname",
                    icon = Icons.Default.Person,
                    keyboardType = KeyboardType.Text,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
                )

                Text("Gender", style = MaterialTheme.typography.bodyMedium, color = Gray, fontSize = 14.sp, modifier = Modifier.padding(bottom = 8.dp))

                Box(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
                    ExposedDropdownMenuBox(
                        expanded = expandedGender,
                        onExpandedChange = { expandedGender = !expandedGender }
                    ) {
                        TextFieldWithBorder(
                            value = viewModel.gender, // ⬅️ Usamos gender del VM
                            onValueChange = {},
                            placeholder = "Select your gender",
                            icon = Icons.Default.Person,
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            readOnly = true,
                            trailingIcon = {
                                Icon(Icons.Default.ExpandMore, "Expand", tint = Gray, modifier = Modifier.size(20.dp))
                            }
                        )
                        ExposedDropdownMenu(
                            expanded = expandedGender,
                            onDismissRequest = { expandedGender = false },
                            modifier = Modifier.background(White).border(1.dp, BorderGray, RoundedCornerShape(12.dp))
                        ) {
                            // Opciones simples mapeadas al backend
                            listOf("Masculino", "Femenino", "Otro").forEach { gender ->
                                DropdownMenuItem(
                                    text = { Text(gender, color = PrimaryColor, fontSize = 16.sp) },
                                    onClick = {
                                        viewModel.gender = gender // ⬅️ GUARDAMOS
                                        expandedGender = false
                                    },
                                    modifier = Modifier.background(White)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 40.dp), horizontalArrangement = Arrangement.End) {
                    TextButton(
                        onClick = {
                            // 🚀 LLAMAMOS AL REGISTRO REAL
                            viewModel.registerUser()
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.tertiary),
                        // Deshabilitar si está cargando o faltan datos
                        enabled = viewModel.nickname.isNotEmpty() && !viewModel.isLoading
                    ) {
                        if (viewModel.isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        } else {
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

enum class Gender(val displayName: String) {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other"),
    PREFER_NOT_TO_SAY("Prefer not to say")
}

private fun isFormValid(
    nickname: String,
    gender: Gender?
): Boolean {
    return nickname.isNotEmpty() && gender != null
}

@Composable
fun TextFieldWithBorder(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(White)
            .border(
                width = 1.dp,
                color = if (isFocused) PrimaryColor else BorderGray,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (trailingIcon != null) Arrangement.SpaceBetween else Arrangement.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isFocused) PrimaryColor else Gray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = PrimaryColor,
                    fontSize = 16.sp
                ),
                cursorBrush = SolidColor(PrimaryColor),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                singleLine = true,
                readOnly = readOnly,
                decorationBox = { innerTextField ->
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = Gray,
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )

            trailingIcon?.invoke()
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PersonalIdentityScreenPreview() {
    SkillShareTheme {
        val mockViewModel = remember { RegisterViewModel() }
        PersonalIdentityScreen(
            viewModel = mockViewModel,
            onContinueClick = {},
            onBackClick = {}
        )
    }
}