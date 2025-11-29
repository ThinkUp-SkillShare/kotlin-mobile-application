package com.skillshare.skilshare_mentor.home

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skillshare.skilshare_mentor.ui.theme.*

@Composable
fun DashboardBottomBar(
    selectedTab: DashboardTab,
    onTabSelected: (DashboardTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = modifier.height(80.dp)
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(24.dp)
                )
            },
            selected = selectedTab == DashboardTab.Home,
            onClick = { onTabSelected(DashboardTab.Home) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryColor,
                unselectedIconColor = Gray,
                indicatorColor = PrimaryColor.copy(alpha = 0.1f)
            )
        )
        /*
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = "Groups",
                    modifier = Modifier.size(24.dp)
                )
            },
            selected = selectedTab == DashboardTab.Groups,
            onClick = { onTabSelected(DashboardTab.Groups) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryColor,
                unselectedIconColor = Gray,
                indicatorColor = PrimaryColor.copy(alpha = 0.1f)
            )
        )
        */

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Calendar",
                    modifier = Modifier.size(24.dp)
                )
            },
            selected = selectedTab == DashboardTab.Calendar,
            onClick = { onTabSelected(DashboardTab.Calendar) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryColor,
                unselectedIconColor = Gray,
                indicatorColor = PrimaryColor.copy(alpha = 0.1f)
            )
        )
        /*
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Folder,
                    contentDescription = "Files",
                    modifier = Modifier.size(24.dp)
                )
            },
            selected = selectedTab == DashboardTab.Files,
            onClick = { onTabSelected(DashboardTab.Files) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryColor,
                unselectedIconColor = Gray,
                indicatorColor = PrimaryColor.copy(alpha = 0.1f)
            )
        )
        */

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Analytics,
                    contentDescription = "Statistics",
                    modifier = Modifier.size(24.dp)
                )
            },
            selected = selectedTab == DashboardTab.Statistics,
            onClick = { onTabSelected(DashboardTab.Statistics) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryColor,
                unselectedIconColor = Gray,
                indicatorColor = PrimaryColor.copy(alpha = 0.1f)
            )
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(24.dp)
                )
            },
            selected = selectedTab == DashboardTab.Profile,
            onClick = { onTabSelected(DashboardTab.Profile) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryColor,
                unselectedIconColor = Gray,
                indicatorColor = PrimaryColor.copy(alpha = 0.1f)
            )
        )
    }
}