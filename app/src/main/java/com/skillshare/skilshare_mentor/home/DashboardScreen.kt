package com.skillshare.skilshare_mentor.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.skillshare.skilshare_mentor.R
import com.skillshare.skilshare_mentor.calendar.CalendarContent
import com.skillshare.skilshare_mentor.profile.ProfileContent
import com.skillshare.skilshare_mentor.statistics.StatisticsContent
import com.skillshare.skilshare_mentor.groups.CreateGroupScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    userName: String,
    userLastName: String,
    institution: String,
    currentUserId: Int,
    onSettingsClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    initialTab: DashboardTab = DashboardTab.Home
) {
    var selectedTab by remember { mutableStateOf(initialTab) }
    val createGroupViewModel: com.skillshare.skilshare_mentor.groups.CreateGroupViewModel = viewModel()
    val context = LocalContext.current
    val onBackToHome = { selectedTab = DashboardTab.Home }

    Scaffold(
        topBar = {
            if (selectedTab != DashboardTab.Home) {
                val title = when (selectedTab) {
                    DashboardTab.CreateGroup -> stringResource(R.string.create_group_title)
                    DashboardTab.Calendar -> stringResource(R.string.calendar_title)
                    DashboardTab.Statistics -> stringResource(R.string.stats_title)
                    DashboardTab.Profile -> stringResource(R.string.home_profile_desc)
                    else -> ""
                }

                TopAppBar(
                    title = { Text(title) },
                    navigationIcon = {
                        IconButton(onClick = onBackToHome) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    },
                    actions = {
                        if (selectedTab == DashboardTab.Profile) {
                            IconButton(onClick = onSettingsClick) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = stringResource(R.string.settings_title),
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            when (selectedTab) {
                DashboardTab.Home -> HomeContent(
                    userName = userName,
                    onNavigate = { newTab -> selectedTab = newTab },
                    onSettingsClick = onSettingsClick,
                    onEditProfileClick = onEditProfile
                )
                DashboardTab.CreateGroup -> {
                    if (createGroupViewModel.isCreated) {
                        selectedTab = DashboardTab.Home
                        createGroupViewModel.resetState()
                        Toast.makeText(context, "¡Grupo creado!", Toast.LENGTH_SHORT).show()
                    }

                    if (createGroupViewModel.createError != null) {
                        Toast.makeText(context, createGroupViewModel.createError, Toast.LENGTH_LONG).show()
                    }

                    CreateGroupScreen(
                        onCreateClick = { name, subject, topic, description, privacy ->
                            createGroupViewModel.createGroup(
                                name, subject, topic, description, privacy,
                                creatorId = currentUserId
                            )
                        }
                    )
                }
                DashboardTab.Calendar -> CalendarContent()
                DashboardTab.Statistics -> StatisticsContent()
                DashboardTab.Profile -> ProfileContent(
                    firstName = userName,
                    lastName = userLastName,
                    institution = institution
                )
            }
        }
    }
}

enum class DashboardTab {
    Home,
    CreateGroup,
    Calendar,
    Statistics,
    Profile
}