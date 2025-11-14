package com.skillshare.skilshare_mentor.home

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.skillshare.skilshare_mentor.calendar.CalendarContent
import com.skillshare.skilshare_mentor.files.FilesContent
import com.skillshare.skilshare_mentor.groups.GroupDetailScreen
import com.skillshare.skilshare_mentor.groups.GroupsContent
import com.skillshare.skilshare_mentor.groups.GroupTab
import com.skillshare.skilshare_mentor.groups.entity.Group
import com.skillshare.skilshare_mentor.profile.ProfileContent
import com.skillshare.skilshare_mentor.statistics.StatisticsContent
import com.skillshare.skilshare_mentor.groups.CreateGroupScreen
import com.skillshare.skilshare_mentor.groups.GroupDetailScreen
import com.skillshare.skilshare_mentor.groups.GroupsContent
import com.skillshare.skilshare_mentor.groups.entity.GroupCategory

@Composable
fun DashboardScreen(
    onSettingsClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    initialTab: DashboardTab = DashboardTab.Home
) {
    var selectedScreen by remember { mutableStateOf(initialTab) }
    var selectedGroup by remember { mutableStateOf<Group?>(null) }
    var selectedGroupTab by remember { mutableStateOf(GroupTab.CHAT) }

    var isCreatingGroup by remember { mutableStateOf(false) }

    if (selectedGroup != null) {
        // 1. Si hay un grupo seleccionado, muestra el detalle
        GroupDetailScreen(
            group = selectedGroup!!,
            onBackClick = {
                selectedGroup = null
                selectedScreen = DashboardTab.Groups // Regresa a la pestaña de grupos
            },
            onTabSelected = { tab -> selectedGroupTab = tab },
            selectedTab = selectedGroupTab
        )
    } else if (isCreatingGroup) {
        // 2. Si estamos creando un grupo, muestra la pantalla de creación
        CreateGroupScreen(
            onBackClick = {
                isCreatingGroup = false // Regresa al dashboard normal
            },
            onCreateClick = { name, subject, topic, description, privacy ->
                // Creamos un grupo de ejemplo
                val newGroup = Group(
                    id = "new_${System.currentTimeMillis()}",
                    name = name,
                    description = description,
                    // Puedes usar 'subject' o 'topic' para elegir categoría
                    category = GroupCategory.PROGRAMMING, // Usamos un placeholder
                    memberCount = 1, // El creador
                    lastActive = "Just now",
                    createdDate = "Today",
                    imageUrl = "https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=400" // Placeholder
                )

                // 1. Cierra la pantalla de creación
                isCreatingGroup = false
                // 2. Asigna el nuevo grupo a 'selectedGroup'
                //    Esto hará que se muestre GroupDetailScreen automáticamente
                selectedGroup = newGroup
            }
        )
    } else {
        // 3. Si no, muestra el dashboard principal
        DashboardScaffold(
            selectedTab = selectedScreen,
            onTabSelected = { selectedScreen = it },
            onSettingsClick = onSettingsClick,
            onLogout = onLogout,
            onEditProfile = onEditProfile
        ) {
            when (selectedScreen) {
                DashboardTab.Home -> HomeContent()
                DashboardTab.Groups -> GroupsContent(
                    onGroupClick = { group ->
                        selectedGroup = group
                    },
                    // ⬇️ Pasa la lambda al GroupsContent ⬇️
                    onCreateGroupClick = {
                        isCreatingGroup = true
                    }
                )
                DashboardTab.Calendar -> CalendarContent()
                DashboardTab.Files -> FilesContent()
                DashboardTab.Statistics -> StatisticsContent()
                DashboardTab.Profile -> ProfileContent()
            }
        }
    }
}

enum class DashboardTab {
    Home, Groups, Calendar, Files, Statistics, Profile
}