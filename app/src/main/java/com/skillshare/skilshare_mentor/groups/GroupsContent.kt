package com.skillshare.skilshare_mentor.groups

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.skillshare.skilshare_mentor.chat.ChatScreen
import com.skillshare.skilshare_mentor.groups.entity.Group
import com.skillshare.skilshare_mentor.groups.entity.GroupCategory
import com.skillshare.skilshare_mentor.ui.theme.PrimaryColor

@Composable
fun GroupsContent(
    onGroupClick: (Group) -> Unit,
    onCreateGroupClick: () -> Unit
) {
    GroupsListContent(
        onGroupClick = onGroupClick,
        onCreateGroupClick = onCreateGroupClick
    )
}

@Composable
fun GroupsListContent(
    onGroupClick: (Group) -> Unit,
    onCreateGroupClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<GroupCategory?>(null) }

    val groups = remember { getSampleGroups() }
    val filteredGroups = remember(groups, searchQuery, selectedCategory) {
        groups.filter { group ->
            (searchQuery.isEmpty() || group.name.contains(searchQuery, ignoreCase = true) ||
                    group.description.contains(searchQuery, ignoreCase = true)) &&
                    (selectedCategory == null || group.category == selectedCategory)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        GroupsHeader(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            onCreateGroupClick = onCreateGroupClick
        )

        SearchBar(
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        )

        CategoryFilterChips(
            selectedCategory = selectedCategory,
            onCategorySelected = { selectedCategory = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        )

        GroupsListWithAds(
            groups = filteredGroups,
            onGroupClick = onGroupClick,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun GroupsHeader(
    modifier: Modifier = Modifier,
    onCreateGroupClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "My Groups",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2C3E50)
        )

        FloatingActionButton(
            onClick = { onCreateGroupClick() },
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            containerColor = PrimaryColor,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 4.dp,
                pressedElevation = 8.dp
            )
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create new group",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color(0xFF7F8C8D)
            )
        },
        placeholder = {
            Text(
                text = "Search by name, description...",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF7F8C8D).copy(alpha = 0.6f)
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = PrimaryColor,
            unfocusedIndicatorColor = Color(0xFFE0E0E0),
            focusedTextColor = Color(0xFF2C3E50),
            unfocusedTextColor = Color(0xFF2C3E50)
        ),
        singleLine = true
    )
}

@Composable
fun CategoryFilterChips(
    selectedCategory: GroupCategory?,
    onCategorySelected: (GroupCategory?) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = GroupCategory.entries
    val scrollState = rememberScrollState()

    Column(modifier = modifier) {
        Text(
            text = "Categories",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF2C3E50),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CategoryChip(
                category = null,
                displayName = "All",
                isSelected = selectedCategory == null,
                onClick = { onCategorySelected(null) }
            )

            categories.forEach { category ->
                CategoryChip(
                    category = category,
                    displayName = category.displayName,
                    isSelected = selectedCategory == category,
                    onClick = { onCategorySelected(category) }
                )
            }
        }
    }
}

@Composable
fun CategoryChip(
    category: GroupCategory?,
    displayName: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        category?.color ?: PrimaryColor
    } else {
        Color.White
    }

    val textColor = if (isSelected) {
        Color.White
    } else {
        Color(0xFF2C3E50)
    }

    val borderColor = if (isSelected) {
        Color.Transparent
    } else {
        Color(0xFFE0E0E0)
    }

    Surface(
        onClick = onClick,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            ),
        color = backgroundColor,
        shadowElevation = if (isSelected) 2.dp else 0.dp
    ) {
        Text(
            text = displayName,
            style = MaterialTheme.typography.labelMedium,
            color = textColor,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        )
    }
}

@Composable
fun GroupsListWithAds(
    groups: List<Group>,
    onGroupClick: (Group) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = remember(groups) {
        val list = mutableListOf<Any>()
        groups.forEachIndexed { index, group ->
            list.add(group)
            if ((index + 1) % 3 == 0) {
                list.add("sponsored")
            }
        }
        list
    }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items) { item ->
            when (item) {
                is Group -> {
                    GroupCard(
                        group = item,
                        onGroupClick = onGroupClick
                    )
                }
                "sponsored" -> {
                    SponsoredContentCard()
                }
            }
        }

        if (groups.isEmpty()) {
            item {
                EmptyGroupsState(
                    modifier = Modifier
                        .fillParentMaxSize(0.8f)
                        .padding(vertical = 40.dp)
                )
            }
        }
    }
}

@Composable
fun GroupCard(
    group: Group,
    onGroupClick: (Group) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = { onGroupClick(group) },
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = group.imageUrl,
                    contentDescription = "Group image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFBBE1FA))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = group.category.displayName,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.Black,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Normal,
                            letterSpacing = 0.5.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = group.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2C3E50),
                    ),
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PeopleOutline,
                        contentDescription = "Members",
                        tint = Color(0xFF7F8C8D),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${group.memberCount} members",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFF7F8C8D),
                            fontSize = 12.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Last active",
                        tint = Color(0xFF7F8C8D),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = group.lastActive,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFF7F8C8D),
                            fontSize = 12.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    onClick = { onGroupClick(group) },
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp)),
                    color = Color(0xFF0F4C75)
                ) {
                    Text(
                        text = "Go to Group",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun SponsoredContentCard() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Campaign,
                contentDescription = "Sponsored",
                tint = Color(0xFFFFA000),
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Sponsored Content",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF7F8C8D)
                ),
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "View",
                tint = Color(0xFFBDBDBD),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun EmptyGroupsState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color(0xFFECF0F1)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.People,
                contentDescription = null,
                tint = Color(0xFF7F8C8D),
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "No groups found",
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color(0xFF2C3E50),
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Try adjusting your search filters\nor create a new group to get started",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color(0xFF7F8C8D)
            ),
            textAlign = TextAlign.Center,
            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.3
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {  },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryColor
            )
        ) {
            Text(
                text = "Create my first group",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

private fun getSampleGroups(): List<Group> {
    return listOf(
        Group(
            id = "1",
            name = "The joke of Peruvian...",
            description = "Exploring political humor and analysis",
            category = GroupCategory.SOCIAL_SCIENCES,
            memberCount = 12,
            lastActive = "2 days ago",
            createdDate = "2024-01-15",
            imageUrl = "https://images.unsplash.com/photo-1580137189272-c9379f8864fd?w=400"
        ),
        Group(
            id = "2",
            name = "Fundamentals of ...",
            description = "Learn basic programming concepts and algorithms",
            category = GroupCategory.PROGRAMMING,
            memberCount = 8,
            lastActive = "1 day ago",
            createdDate = "2024-02-10",
            imageUrl = "https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=400"
        ),
        Group(
            id = "3",
            name = "Learning from Classic..",
            description = "Study and discuss classic film masterpieces",
            category = GroupCategory.ARTS,
            memberCount = 10,
            lastActive = "1 day ago",
            createdDate = "2024-01-20",
            imageUrl = "https://antologiaglobal.com/wp-content/uploads/2022/03/pexels-tima-miroshnichenko-7991579.jpg"
        ),
        Group(
            id = "4",
            name = "Literature in Paris",
            description = "Exploring French literature and authors",
            category = GroupCategory.LITERATURE,
            memberCount = 5,
            lastActive = "2 days ago",
            createdDate = "2024-03-05",
            imageUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400"
        ),
        Group(
            id = "5",
            name = "Philosophy in the Network",
            description = "Modern philosophical discussions online",
            category = GroupCategory.SOCIAL_SCIENCES,
            memberCount = 8,
            lastActive = "3 days ago",
            createdDate = "2024-02-28",
            imageUrl = "https://images.unsplash.com/photo-1588072432836-e10032774350?w=400"
        ),
        Group(
            id = "6",
            name = "Advanced Mathematics",
            description = "Complex problem solving and theory",
            category = GroupCategory.MATHEMATICS,
            memberCount = 15,
            lastActive = "4 hours ago",
            createdDate = "2024-01-10",
            imageUrl = "https://images.unsplash.com/photo-1635070041078-e363dbe005cb?w=400"
        )
    )
}