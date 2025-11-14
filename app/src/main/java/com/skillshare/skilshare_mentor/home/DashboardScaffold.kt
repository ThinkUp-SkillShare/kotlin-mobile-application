package com.skillshare.skilshare_mentor.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.skillshare.skilshare_mentor.profile.ProfileTopBar

@Composable
fun DashboardScaffold(
    selectedTab: DashboardTab,
    onTabSelected: (DashboardTab) -> Unit,
    onSettingsClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            if (selectedTab == DashboardTab.Profile) {
                ProfileTopBar(
                    onEditProfile = onEditProfile,
                    onSettingsClick = onSettingsClick
                )
            }
        },
        bottomBar = {
            DashboardBottomBar(
                selectedTab = selectedTab,
                onTabSelected = onTabSelected
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            content()
        }
    }
}