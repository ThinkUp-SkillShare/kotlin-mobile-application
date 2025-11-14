package com.skillshare.skilshare_mentor.register

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
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInfoScreen(
    onContinueClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var selectedDay by remember { mutableStateOf("") }
    var selectedMonth by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var expandedDay by remember { mutableStateOf(false) }
    var expandedMonth by remember { mutableStateOf(false) }

    val days = List(31) { i ->
        String.format("%02d", i + 1)
    }

    val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
    val maxYear = currentYear - 14
    val years = (1950..maxYear).toList()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp)
            ) {

                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("file:///android_asset/images/common/foxdungee/about_yourself.png")
                        .build()
                )


                Image(
                    painter = painter,
                    contentDescription = "About Yourself",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Text(
                    text = "Tell us a little about yourself",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                TextFieldWithBorder(
                    value = firstName,
                    onValueChange = { firstName = it },
                    placeholder = "First name",
                    icon = Icons.Default.Person,
                    keyboardType = KeyboardType.Text,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                TextFieldWithBorder(
                    value = lastName,
                    onValueChange = { lastName = it },
                    placeholder = "Last name",
                    icon = Icons.Default.Person,
                    keyboardType = KeyboardType.Text,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                )

                Text(
                    text = "Date of birth",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gray,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = expandedDay,
                            onExpandedChange = { expandedDay = !expandedDay }
                        ) {
                            DateFieldWithBorder(
                                value = selectedDay,
                                onValueChange = {},
                                placeholder = "DD",
                                icon = Icons.Default.Cake,
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDay)
                                }
                            )
                            ExposedDropdownMenu(
                                expanded = expandedDay,
                                onDismissRequest = { expandedDay = false },
                                modifier = Modifier
                                    .background(White)
                                    .border(
                                        width = 1.dp,
                                        color = BorderGray,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                            ) {
                                days.forEach { day ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = day,
                                                color = PrimaryColor,
                                                fontSize = 16.sp,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        },
                                        onClick = {
                                            selectedDay = day
                                            expandedDay = false
                                        },
                                        modifier = Modifier.background(White)
                                    )
                                }
                            }
                        }
                    }

                    Box(
                        modifier = Modifier.weight(1.5f)
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = expandedMonth,
                            onExpandedChange = { expandedMonth = !expandedMonth }
                        ) {
                            DateFieldWithBorder(
                                value = selectedMonth,
                                onValueChange = {},
                                placeholder = "Month",
                                icon = Icons.Default.CalendarMonth,
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMonth)
                                }
                            )
                            ExposedDropdownMenu(
                                expanded = expandedMonth,
                                onDismissRequest = { expandedMonth = false },
                                modifier = Modifier
                                    .background(White)
                                    .border(
                                        width = 1.dp,
                                        color = BorderGray,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                            ) {
                                months.forEach { month ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = month,
                                                color = PrimaryColor,
                                                fontSize = 16.sp,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        },
                                        onClick = {
                                            selectedMonth = month
                                            expandedMonth = false
                                        },
                                        modifier = Modifier.background(White)
                                    )
                                }
                            }
                        }
                    }

                    Box(
                        modifier = Modifier.weight(1.2f)
                    ) {
                        var expandedYear by remember { mutableStateOf(false) }

                        ExposedDropdownMenuBox(
                            expanded = expandedYear,
                            onExpandedChange = { expandedYear = !expandedYear }
                        ) {
                            DateFieldWithBorder(
                                value = year,
                                onValueChange = {},
                                placeholder = "YYYY",
                                icon = Icons.Default.CalendarMonth,
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedYear)
                                }
                            )
                            ExposedDropdownMenu(
                                expanded = expandedYear,
                                onDismissRequest = { expandedYear = false },
                                modifier = Modifier
                                    .background(White)
                                    .border(
                                        width = 1.dp,
                                        color = BorderGray,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                            ) {
                                years.reversed().forEach { yearValue ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = yearValue.toString(),
                                                color = PrimaryColor,
                                                fontSize = 16.sp,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        },
                                        onClick = {
                                            year = yearValue.toString()
                                            expandedYear = false
                                        },
                                        modifier = Modifier.background(White)
                                    )
                                }
                            }
                        }
                    }
                }

                if (selectedDay.isEmpty() || selectedMonth.isEmpty() || year.isEmpty()) {
                    Text(
                        text = "Please select day, month and year",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 40.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            if (isFormValid(
                                    firstName,
                                    lastName,
                                    selectedDay,
                                    selectedMonth,
                                    year
                                )
                            ) {
                                onContinueClick()
                            }
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.tertiary
                        ),
                        enabled = isFormValid(firstName, lastName, selectedDay, selectedMonth, year)
                    ) {
                        Text(
                            text = "Continue",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Continue",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun isFormValid(
    firstName: String,
    lastName: String,
    day: String,
    month: String,
    year: String
): Boolean {
    return firstName.isNotEmpty() &&
            lastName.isNotEmpty() &&
            day.isNotEmpty() &&
            month.isNotEmpty() &&
            year.isNotEmpty()
}

@Composable
fun TextFieldWithBorder(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    keyboardType: KeyboardType,
    modifier: Modifier = Modifier
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
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isFocused) PrimaryColor else Gray,
                modifier = Modifier.size(20.dp)
            )

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
        }
    }
}

@Composable
fun DateFieldWithBorder(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
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
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (trailingIcon != null) Arrangement.SpaceBetween else Arrangement.spacedBy(
                8.dp
            )
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isFocused) PrimaryColor else Gray,
                modifier = Modifier.size(18.dp)
            )

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = if (value.isEmpty()) Gray else PrimaryColor,
                    fontSize = 10.sp
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
                                fontSize = 10.sp

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