package com.skillshare.skilshare_mentor.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.skillshare.skilshare_mentor.R
import com.skillshare.skilshare_mentor.profile.entity.Teacher

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    teacher: Teacher,
    onSave: (Teacher) -> Unit,
    onBack: () -> Unit
) {
    var firstName by remember { mutableStateOf(teacher.firstName) }
    var lastName by remember { mutableStateOf(teacher.lastName) }
    var nickname by remember { mutableStateOf(teacher.nickname) }
    var educationalCenter by remember { mutableStateOf(teacher.educationalCenter) }
    var country by remember { mutableStateOf(teacher.country) }
    var gender by remember { mutableStateOf(teacher.gender) }

    val dateBirth = teacher.dateBirth
    val universityEmail = teacher.universityEmail
    val universityDocument = teacher.universityDocument

    Scaffold(
        topBar = {
            EditProfileTopBar(
                onBack = onBack,
                onSave = {
                    val updatedTeacher = teacher.copy(
                        firstName = firstName,
                        lastName = lastName,
                        nickname = nickname,
                        educationalCenter = educationalCenter,
                        country = country,
                        gender = gender
                    )
                    onSave(updatedTeacher)
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            ProfilePictureSection(
                coverUrl = teacher.cover,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))

            EditProfileTextField(
                label = stringResource(R.string.label_name),
                value = firstName,
                onValueChange = { firstName = it },
                leadingIcon = Icons.Default.Person
            )

            EditProfileTextField(
                label = stringResource(R.string.label_lastname),
                value = lastName,
                onValueChange = { lastName = it },
                leadingIcon = Icons.Default.PersonOutline
            )

            EditProfileTextField(
                label = stringResource(R.string.label_nickname),
                value = nickname,
                onValueChange = { nickname = it },
                leadingIcon = Icons.Default.AlternateEmail
            )

            DisabledTextField(
                label = stringResource(R.string.label_birthday),
                value = formatBirthday(dateBirth),
                leadingIcon = Icons.Default.Cake
            )

            DisabledTextField(
                label = stringResource(R.string.label_uni_email),
                value = universityEmail,
                leadingIcon = Icons.Default.Email
            )

            DisabledTextField(
                label = stringResource(R.string.label_uni_id),
                value = universityDocument,
                leadingIcon = Icons.Default.Badge
            )

            // --- MÁS CAMPOS EDITABLES ---

            EditProfileTextField(
                label = stringResource(R.string.label_institution),
                value = educationalCenter,
                onValueChange = { educationalCenter = it },
                leadingIcon = Icons.Default.School
            )

            EditProfileTextField(
                label = stringResource(R.string.label_country),
                value = country,
                onValueChange = { country = it },
                leadingIcon = Icons.Default.Public
            )

            GenderDropdown(
                selectedGender = gender,
                onGenderSelected = { gender = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de Guardar (Inferior)
            SaveButton(
                onClick = {
                    val updatedTeacher = teacher.copy(
                        firstName = firstName,
                        lastName = lastName,
                        nickname = nickname,
                        educationalCenter = educationalCenter,
                        country = country,
                        gender = gender
                    )
                    onSave(updatedTeacher)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileTopBar(
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.edit_profile_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        actions = {
            TextButton(onClick = onSave) {
                Text(
                    text = stringResource(R.string.btn_save_changes),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
fun ProfilePictureSection(
    coverUrl: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .shadow(elevation = 4.dp, shape = CircleShape, clip = false)
        ) {
            AsyncImage(
                model = coverUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,

            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisabledTextField(
    label: String,
    value: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector
) {
    TextField(
        value = value,
        onValueChange = { },
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        enabled = false,
        colors = TextFieldDefaults.colors(
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            disabledIndicatorColor = Color.Transparent,
            disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderDropdown(
    selectedGender: String,
    onGenderSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val genderOptions = listOf("Masculino", "Femenino", "Otro", "Prefiero no decir")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedGender,
            onValueChange = { },
            label = {
                Text(
                    text = stringResource(R.string.label_gender),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(12.dp),
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        ) {
            genderOptions.forEach { gender ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = gender,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    onClick = {
                        onGenderSelected(gender)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun SaveButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text = stringResource(R.string.btn_save_changes),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

private fun formatBirthday(dateBirth: String): String {
    return when (dateBirth) {
        "1998-03-15" -> "15 de marzo, 1998"
        else -> dateBirth
    }
}