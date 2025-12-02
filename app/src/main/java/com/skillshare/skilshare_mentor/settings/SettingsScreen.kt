package com.skillshare.skilshare_mentor.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skillshare.skilshare_mentor.ui.theme.AppTheme
import androidx.compose.ui.res.stringResource
import com.skillshare.skilshare_mentor.R

data class LanguageOption(val code: String, val name: String, val flag: String)
val languages = listOf(
    LanguageOption("en", "English", "🇺🇸"),
    LanguageOption("es", "Español", "🇲🇽")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    currentTheme: AppTheme,
    onThemeChange: (AppTheme) -> Unit,
    currentLanguageCode: String,
    onLanguageChange: (String) -> Unit
) {
    val selectedLanguage = languages.find { it.code == currentLanguageCode } ?: languages.first()
    var showLanguageSheet by remember { mutableStateOf(false) }
    var showThemeSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
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
                title = stringResource(R.string.language),
                subtitle = selectedLanguage.name,
                onClick = { showLanguageSheet = true }
            )

            SettingItem(
                icon = Icons.Outlined.Palette,
                title = stringResource(R.string.theme),
                subtitle = currentTheme.name,
                onClick = { showThemeSheet = true }
            )

        }
    }

    if (showLanguageSheet) {
        LanguageBottomSheet(
            onDismiss = { showLanguageSheet = false },
            currentLanguage = selectedLanguage,
            onLanguageSelected = { language ->
                onLanguageChange(language.code)
                showLanguageSheet = false
            }
        )
    }
    if (showThemeSheet) {
        ThemeBottomSheet(
            onDismiss = { showThemeSheet = false },
            currentTheme = currentTheme,
            onThemeSelected = { newTheme ->
                onThemeChange(newTheme)
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            if (hasArrow) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThemeBottomSheet(
    onDismiss: () -> Unit,
    currentTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.fillMaxWidth().padding(20.dp)) {
            Text("Theme", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))

            val options = listOf(
                Triple(AppTheme.Light, "Light", Icons.Default.LightMode),
                Triple(AppTheme.Dark, "Dark", Icons.Default.DarkMode),
                Triple(AppTheme.System, "System", Icons.Default.SettingsSystemDaydream)
            )

            options.forEach { (theme, name, icon) ->
                val isSelected = theme == currentTheme
                SelectableOptionRow(
                    text = name,
                    icon = icon,
                    isSelected = isSelected,
                    onClick = { onThemeSelected(theme) }
                )
                Spacer(Modifier.height(8.dp))
            }
            Spacer(Modifier.height(20.dp))
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
        Column(Modifier.fillMaxWidth().padding(20.dp)) {
            Text("Language", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            languages.forEach { lang ->
                SelectableOptionRow(
                    text = "${lang.flag}  ${lang.name}",
                    isSelected = lang == currentLanguage,
                    onClick = { onLanguageSelected(lang) }
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
    val primaryColor = MaterialTheme.colorScheme.primary
    val contentColor = MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (isSelected) primaryColor.copy(alpha = 0.5f) else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = if (isSelected) primaryColor.copy(alpha = 0.1f) else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = null, tint = contentColor)
            Spacer(Modifier.width(16.dp))
        }
        Text(
            text = text,
            color = contentColor,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
        Spacer(Modifier.weight(1f))
        if (isSelected) {
            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = primaryColor)
        }
    }
}