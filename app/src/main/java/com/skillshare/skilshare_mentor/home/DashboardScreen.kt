package com.skillshare.skilshare_mentor.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.skillshare.skilshare_mentor.calendar.CalendarContent
import com.skillshare.skilshare_mentor.files.FilesContent
import com.skillshare.skilshare_mentor.groups.CreateGroupScreen
import com.skillshare.skilshare_mentor.groups.GroupDetailScreen
import com.skillshare.skilshare_mentor.groups.GroupsContent
import com.skillshare.skilshare_mentor.groups.GroupTab
import com.skillshare.skilshare_mentor.groups.entity.Group
import com.skillshare.skilshare_mentor.profile.ProfileContent
import com.skillshare.skilshare_mentor.statistics.StatisticsContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onSettingsClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    initialTab: DashboardTab = DashboardTab.Home
) {
    var selectedTab by remember { mutableStateOf(initialTab) }
    var selectedGroup by remember { mutableStateOf<Group?>(null) }
    var isCreatingGroup by remember { mutableStateOf(false) }

    val onBackToHome = { selectedTab = DashboardTab.Home }

    if (selectedGroup != null) {
        GroupDetailScreen(
            group = selectedGroup!!,
            onBackClick = { selectedGroup = null },
            onTabSelected = { },
            selectedTab = GroupTab.CHAT
        )
    } else if (isCreatingGroup) {
        CreateGroupScreen(
            onBackClick = { isCreatingGroup = false },
            onCreateClick = { _, _, _, _, _ -> isCreatingGroup = false }
        )
    } else {
        Scaffold(
            topBar = {
                if (selectedTab != DashboardTab.Home) {
                    TopAppBar(
                        title = { Text(getScreenTitle(selectedTab)) },
                        navigationIcon = {
                            IconButton(onClick = onBackToHome) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                            }
                        },
                        actions = {
                            if (selectedTab == DashboardTab.Profile) {
                                IconButton(onClick = onSettingsClick) {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = "Configuración",
                                        tint = Color.Black
                                    )
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.White
                        )
                    )
                }
            }
        ) { paddingValues ->
            Surface(modifier = Modifier.padding(paddingValues)) {
                when (selectedTab) {
                    DashboardTab.Home -> HomeContent(
                        onNavigate = { newTab -> selectedTab = newTab },
                        onSettingsClick = onSettingsClick,
                        onEditProfileClick = onEditProfile
                    )
                    /*
                    DashboardTab.Groups -> GroupsContent(
                        onGroupClick = { group -> selectedGroup = group },
                        onCreateGroupClick = { isCreatingGroup = true }
                    )
                    */
                    DashboardTab.Calendar -> CalendarContent()
                    //DashboardTab.Files -> FilesContent()
                    DashboardTab.Statistics -> StatisticsContent()
                    DashboardTab.Profile -> ProfileContent()
                }
            }
        }
    }
}

fun getScreenTitle(tab: DashboardTab): String {
    return when (tab) {
        //DashboardTab.Groups -> "Mis Grupos"
        DashboardTab.Calendar -> "Calendario"
        //DashboardTab.Files -> "Mis Archivos"
        DashboardTab.Statistics -> "Estadísticas"
        DashboardTab.Profile -> "Mi Perfil"
        else -> ""
    }
}

enum class DashboardTab {
    Home,
    //Groups,
    Calendar,
    //Files,
    Statistics,
    Profile
}