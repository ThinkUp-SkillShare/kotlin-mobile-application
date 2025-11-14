package com.skillshare.skilshare_mentor.groups

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.skillshare.skilshare_mentor.chat.ChatScreen
import com.skillshare.skilshare_mentor.groups.entity.Group
import com.skillshare.skilshare_mentor.groups.entity.GroupCategory

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailScreen(
    group: Group,
    onBackClick: () -> Unit,
    onTabSelected: (GroupTab) -> Unit,
    selectedTab: GroupTab
) {
    val scrollState = rememberLazyListState()
    var isExpanded by remember { mutableStateOf(true) }

    LaunchedEffect(scrollState.firstVisibleItemIndex) {
        isExpanded = scrollState.firstVisibleItemIndex == 0 &&
                scrollState.firstVisibleItemScrollOffset < 100
    }

    Scaffold(
        topBar = {
            CollapsibleTopBar(
                group = group,
                isExpanded = isExpanded,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            GroupTabs(
                selectedTab = selectedTab,
                onTabSelected = onTabSelected,
                modifier = Modifier.fillMaxWidth()
            )

            GroupContent(
                group = group,
                selectedTab = selectedTab,
                scrollState = scrollState,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun CollapsibleTopBar(
    group: Group,
    isExpanded: Boolean,
    onBackClick: () -> Unit
) {
    val alpha = if (isExpanded) 1f else 0f
    val height = if (isExpanded) 200.dp else 64.dp

    Surface(
        tonalElevation = if (isExpanded) 0.dp else 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver"
                    )
                }

                Text(
                    text = group.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f, fill = false)
                )

                Spacer(modifier = Modifier.size(48.dp))
            }

            if (isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .alpha(alpha),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = group.imageUrl ?: "",
                        contentDescription = "Imagen del grupo ${group.name}",
                        modifier = Modifier
                            .size(80.dp)
                            .padding(8.dp),
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        text = group.description,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                        maxLines = 3
                    )

                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "${group.memberCount} miembros",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = "Últ. actividad: ${group.lastActive}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(onClick = {  }) {
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = "Subir"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GroupTabs(
    selectedTab: GroupTab,
    onTabSelected: (GroupTab) -> Unit,
    modifier: Modifier = Modifier
) {
    TabRow(
        selectedTabIndex = selectedTab.ordinal,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        GroupTab.entries.forEach { tab ->
            Tab(
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
                text = {
                    Text(
                        text = getTabDisplayName(tab),
                        fontSize = 12.sp
                    )
                }
            )
        }
    }
}

private fun getTabDisplayName(tab: GroupTab): String {
    return when (tab) {
        GroupTab.CHAT -> "Chat"
        GroupTab.RESOURCES -> "Resources"
        GroupTab.QUIZZES -> "Quizzes"
        GroupTab.CALLS -> "Calls"
    }
}

@Composable
fun GroupContent(
    group: Group,
    selectedTab: GroupTab,
    scrollState: LazyListState,
    modifier: Modifier = Modifier
) {
    when (selectedTab) {
        GroupTab.CHAT -> {
            ChatScreen(
                groupName = group.name,
                onBackClick = { /* Navegar atrás si es necesario */ }
            )
        }
        GroupTab.RESOURCES -> {
            LazyColumn(
                state = scrollState,
                modifier = modifier,
                contentPadding = PaddingValues(16.dp)
            ) {
                items(15) { index ->
                    ResourceItem(
                        title = "Recurso ${index + 1}",
                        description = "Descripción del recurso",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }
        GroupTab.QUIZZES -> {
            LazyColumn(
                state = scrollState,
                modifier = modifier,
                contentPadding = PaddingValues(16.dp)
            ) {
                items(10) { index ->
                    QuizItem(
                        title = "Quiz ${index + 1}",
                        questions = 5,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }
        GroupTab.CALLS -> {
            LazyColumn(
                state = scrollState,
                modifier = modifier,
                contentPadding = PaddingValues(16.dp)
            ) {
                items(8) { index ->
                    CallItem(
                        date = "2024-01-${index + 1}",
                        duration = "${30 + index} min",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ResourceItem(title: String, description: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontWeight = FontWeight.Bold)
            Text(text = description)
        }
    }
}

@Composable
fun QuizItem(title: String, questions: Int, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontWeight = FontWeight.Bold)
            Text(text = "$questions preguntas")
        }
    }
}

@Composable
fun CallItem(date: String, duration: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Llamada: $date")
            Text(text = duration)
        }
    }
}

enum class GroupTab {
    CHAT, RESOURCES, QUIZZES, CALLS
}