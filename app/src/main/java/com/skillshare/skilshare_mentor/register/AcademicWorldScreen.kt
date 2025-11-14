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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.School
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcademicWorldScreen(
    onContinueClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var selectedCountry by remember { mutableStateOf<Country?>(null) }
    var educationalCenter by remember { mutableStateOf("") }
    var expandedCountry by remember { mutableStateOf(false) }

    val countries = listOf(
        Country("United States", "US"),
        Country("United Kingdom", "GB"),
        Country("Canada", "CA"),
        Country("Australia", "AU"),
        Country("Germany", "DE"),
        Country("France", "FR"),
        Country("Japan", "JP"),
        Country("Brazil", "BR"),
        Country("Mexico", "MX"),
        Country("Spain", "ES"),
        Country("China", "CN"),
        Country("India", "IN"),
        Country("Italy", "IT"),
        Country("South Korea", "KR"),
        Country("Russia", "RU"),
        Country("Argentina", "AR"),
        Country("Chile", "CL"),
        Country("Colombia", "CO"),
        Country("Peru", "PE"),
        Country("Portugal", "PT"),
        Country("Netherlands", "NL"),
        Country("Sweden", "SE"),
        Country("Norway", "NO"),
        Country("Finland", "FI"),
        Country("Denmark", "DK"),
        Country("Switzerland", "CH"),
        Country("Austria", "AT"),
        Country("Belgium", "BE"),
        Country("Ireland", "IE"),
        Country("New Zealand", "NZ"),
        Country("South Africa", "ZA"),
        Country("Egypt", "EG"),
        Country("Turkey", "TR"),
        Country("Saudi Arabia", "SA"),
        Country("United Arab Emirates", "AE"),
        Country("Singapore", "SG"),
        Country("Malaysia", "MY"),
        Country("Thailand", "TH"),
        Country("Vietnam", "VN"),
        Country("Philippines", "PH"),
        Country("Indonesia", "ID"),
        Country("Pakistan", "PK"),
        Country("Bangladesh", "BD"),
        Country("Nigeria", "NG"),
        Country("Kenya", "KE"),
        Country("Ethiopia", "ET")
    )

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
                        .data("file:///android_asset/images/common/foxdungee/academic_world.png")
                        .build()
                )

                Image(
                    painter = painter,
                    contentDescription = "Academic World",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(250.dp, 200.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Text(
                    text = "Your academic world",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Country",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gray,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                ) {
                    ExposedDropdownMenuBox(
                        expanded = expandedCountry,
                        onExpandedChange = { expandedCountry = !expandedCountry }
                    ) {
                        TextFieldWithBorder(
                            value = selectedCountry?.name ?: "",
                            onValueChange = {},
                            placeholder = "Select your country",
                            icon = Icons.Default.LocationOn,
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCountry)
                            },
                            leadingContent = selectedCountry?.let { country ->
                                {
                                    CountryFlagEmoji(countryCode = country.code)
                                }
                            }
                        )
                        ExposedDropdownMenu(
                            expanded = expandedCountry,
                            onDismissRequest = { expandedCountry = false },
                            modifier = Modifier
                                .background(White)
                                .border(
                                    width = 1.dp,
                                    color = BorderGray,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .heightIn(max = 400.dp)
                        ) {
                            countries.forEach { country ->
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            CountryFlagEmoji(countryCode = country.code)
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(
                                                text = country.name,
                                                color = PrimaryColor,
                                                fontSize = 16.sp
                                            )
                                        }
                                    },
                                    onClick = {
                                        selectedCountry = country
                                        expandedCountry = false
                                    },
                                    modifier = Modifier.background(White)
                                )
                            }
                        }
                    }
                }

                Text(
                    text = "Educational Center",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gray,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                TextFieldWithBorder(
                    value = educationalCenter,
                    onValueChange = { educationalCenter = it },
                    placeholder = "Enter your educational center",
                    icon = Icons.Default.School,
                    keyboardType = KeyboardType.Text,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 40.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            if (isFormValid(selectedCountry, educationalCenter)) {
                                onContinueClick()
                            }
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.tertiary
                        ),
                        enabled = isFormValid(selectedCountry, educationalCenter)
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

data class Country(
    val name: String,
    val code: String
)

@Composable
fun CountryFlagEmoji(
    countryCode: String,
    modifier: Modifier = Modifier
) {
    val flagEmoji = remember(countryCode) {
        getFlagEmoji(countryCode)
    }

    Text(
        text = flagEmoji,
        fontSize = 20.sp,
        modifier = modifier
    )
}

private fun getFlagEmoji(countryCode: String): String {
    if (countryCode.length != 2) return "🏳️"

    val firstLetter = Character.codePointAt(countryCode.uppercase(), 0) - 0x41 + 0x1F1E6
    val secondLetter = Character.codePointAt(countryCode.uppercase(), 1) - 0x41 + 0x1F1E6

    return if (firstLetter in 0x1F1E6..0x1F1FF && secondLetter in 0x1F1E6..0x1F1FF) {
        String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
    } else {
        "🏳️"
    }
}

private fun isFormValid(
    country: Country?,
    educationalCenter: String
): Boolean {
    return country != null && educationalCenter.isNotEmpty()
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
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null
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
            if (leadingContent != null) {
                leadingContent()
                Spacer(modifier = Modifier.width(12.dp))
            } else {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isFocused) PrimaryColor else Gray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

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
fun AcademicWorldScreenPreview() {
    SkillShareTheme {
        AcademicWorldScreen(
            onContinueClick = {},
            onBackClick = {}
        )
    }
}