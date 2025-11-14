package com.skillshare.skilshare_mentor.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

private data class LanguageOption(val code: String, val name: String, val flag: String)
private val languages = listOf(
    LanguageOption("en", "English", "🇺🇸"),
    LanguageOption("es", "Spanish", "🇲🇽")
)

private data class ThemeOption(val name: String, val icon: ImageVector)
private val themes = listOf(
    ThemeOption("Light", Icons.Default.LightMode),
    ThemeOption("Dark", Icons.Default.DarkMode),
    ThemeOption("System", Icons.Default.SettingsSystemDaydream)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBackClick: () -> Unit) {
    // --- Estado local para manejar las selecciones ---
    var selectedLanguage by remember { mutableStateOf(languages.first()) }
    var selectedTheme by remember { mutableStateOf(themes.last()) }
    var showLanguageSheet by remember { mutableStateOf(false) }
    var showThemeSheet by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFAFAFA)
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = Color(0xFFFAFAFA)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SettingItem(
                icon = Icons.Default.Language,
                title = "Language",
                subtitle = selectedLanguage.name,
                onClick = { showLanguageSheet = true }
            )
            SettingItem(
                icon = Icons.Outlined.Palette,
                title = "Theme",
                subtitle = selectedTheme.name,
                onClick = { showThemeSheet = true }
            )
            SettingItem(
                icon = Icons.Outlined.Notifications,
                title = "Notifications",
                hasArrow = true,
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Notifications - Coming soon!")
                    }
                }
            )
        }
    }

    if (showLanguageSheet) {
        LanguageBottomSheet(
            onDismiss = { showLanguageSheet = false },
            currentLanguage = selectedLanguage,
            onLanguageSelected = {
                selectedLanguage = it
                showLanguageSheet = false
            }
        )
    }
    if (showThemeSheet) {
        ThemeBottomSheet(
            onDismiss = { showThemeSheet = false },
            currentTheme = selectedTheme,
            onThemeSelected = {
                selectedTheme = it
                showThemeSheet = false
            }
        )
    }
}

@Composable
private fun SettingItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    hasArrow: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.Black.copy(alpha = 0.87f),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black.copy(alpha = 0.87f)
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        color = Color.Gray.copy(alpha = 0.8f)
                    )
                }
            }

            if (hasArrow) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color.Gray.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguageBottomSheet(
    onDismiss: () -> Unit,
    currentLanguage: LanguageOption,
    onLanguageSelected: (LanguageOption) -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .background(Color.Gray.copy(alpha = 0.3f), CircleShape)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(20.dp))

            Text("Language", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))

            languages.forEach { lang ->
                val isSelected = lang == currentLanguage
                SelectableOptionRow(
                    text = "${lang.flag}  ${lang.name}",
                    isSelected = isSelected,
                    onClick = { onLanguageSelected(lang) }
                )
                Spacer(Modifier.height(8.dp))
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThemeBottomSheet(
    onDismiss: () -> Unit,
    currentTheme: ThemeOption,
    onThemeSelected: (ThemeOption) -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .background(Color.Gray.copy(alpha = 0.3f), CircleShape)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(20.dp))

            Text("Theme", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))

            themes.forEach { theme ->
                val isSelected = theme == currentTheme
                SelectableOptionRow(
                    text = theme.name,
                    icon = theme.icon,
                    isSelected = isSelected,
                    onClick = { onThemeSelected(theme) }
                )
                Spacer(Modifier.height(8.dp))
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun SelectableOptionRow(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector? = null
) {
    val color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
    val textColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Black.copy(alpha = 0.87f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = color.copy(alpha = if (isSelected) 0.3f else 0.0f),
                shape = RoundedCornerShape(12.dp)
            )
            .background(color.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor.copy(alpha = 0.8f)
            )
            Spacer(Modifier.width(16.dp))
        }
        Text(
            text = text,
            color = textColor,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
        Spacer(Modifier.weight(1f))
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Selected",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}