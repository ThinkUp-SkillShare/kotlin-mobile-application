package com.skillshare.skilshare_mentor.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.skillshare.skilshare_mentor.R
import com.skillshare.skilshare_mentor.profile.entity.Teacher
import com.skillshare.skilshare_mentor.ui.theme.PrimaryColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    viewModel: EditProfileViewModel,
    userId: Int,
    teacher: Teacher,
    onSaveSuccess: (Teacher) -> Unit,
    onBack: () -> Unit
) {
    // Estados locales para la edición
    var firstName by remember { mutableStateOf(teacher.firstName) }
    var lastName by remember { mutableStateOf(teacher.lastName) }
    var nickname by remember { mutableStateOf(teacher.nickname) }
    var educationalCenter by remember { mutableStateOf(teacher.educationalCenter) }
    var country by remember { mutableStateOf(teacher.country) }
    var gender by remember { mutableStateOf(teacher.gender) }

    val dateBirth = teacher.dateBirth
    val universityEmail = teacher.universityEmail
    val universityDocument = teacher.universityDocument

    LaunchedEffect(viewModel.isSaved) {
        if (viewModel.isSaved) {
            val updatedTeacher = teacher.copy(
                firstName = firstName,
                lastName = lastName,
                nickname = nickname,
                educationalCenter = educationalCenter,
                country = country,
                gender = gender
            )
            onSaveSuccess(updatedTeacher)
            viewModel.isSaved = false
        }
    }

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
                    viewModel.saveProfile(userId, updatedTeacher)
                },
                isLoading = viewModel.isLoading
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
            /*
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
            */
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

            if (viewModel.saveError != null) {
                Text(
                    text = viewModel.saveError!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

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
                    viewModel.saveProfile(userId, updatedTeacher)
                },
                isLoading = viewModel.isLoading,
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
    onSave: () -> Unit,
    isLoading: Boolean
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
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        actions = {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp).padding(end = 16.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                TextButton(onClick = onSave) {
                    Text(
                        text = stringResource(R.string.btn_save),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
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

@Composable
fun EditProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: ImageVector
) {
    var isFocused by remember { mutableStateOf(false) }
    val containerColor = MaterialTheme.colorScheme.surfaceVariant
    val contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    val borderColor = if (isFocused) MaterialTheme.colorScheme.primary else Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(containerColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = if (isFocused) MaterialTheme.colorScheme.primary else contentColor.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                if (value.isNotEmpty()) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        color = contentColor.copy(alpha = 0.7f)
                    )
                }
                androidx.compose.foundation.text.BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { isFocused = it.isFocused },
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        if (value.isEmpty()) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodyMedium,
                                color = contentColor.copy(alpha = 0.5f),
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    }
                )
            }
        }
    }
}

@Composable
fun DisabledTextField(
    label: String,
    value: String,
    leadingIcon: ImageVector
) {
    val containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    val contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(containerColor)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = contentColor
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp,
                    color = contentColor
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderDropdown(
    selectedGender: String,
    onGenderSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val genderOptions = listOf("Masculino", "Femenino", "Otro", "Prefiero no decir")
    val containerColor = MaterialTheme.colorScheme.surfaceVariant
    val contentColor = MaterialTheme.colorScheme.onSurfaceVariant

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .menuAnchor()
                .clip(RoundedCornerShape(12.dp))
                .background(containerColor)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = null,
                    tint = contentColor.copy(alpha = 0.7f),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.label_gender),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        color = contentColor.copy(alpha = 0.7f)
                    )
                    Text(
                        text = selectedGender,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = contentColor.copy(alpha = 0.7f)
                )
            }
        }

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
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(50.dp),
        shape = RoundedCornerShape(12.dp),
        enabled = !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
        } else {
            Text(
                text = stringResource(R.string.btn_save_changes),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

private fun formatBirthday(dateBirth: String): String {
    return when (dateBirth) {
        "1998-03-15" -> "15 de marzo, 1998"
        else -> dateBirth
    }
}