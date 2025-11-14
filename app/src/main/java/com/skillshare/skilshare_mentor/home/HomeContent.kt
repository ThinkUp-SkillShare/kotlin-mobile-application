package com.skillshare.skilshare_mentor.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skillshare.skilshare_mentor.R
import com.skillshare.skilshare_mentor.ui.theme.Gray
import com.skillshare.skilshare_mentor.ui.theme.PrimaryColor

val imageMap = mapOf(
    "alan" to R.drawable.alan,
    "anatomia" to R.drawable.anatomia,
    "arte" to R.drawable.arte,
    "biologia" to R.drawable.biologia,
    "cine" to R.drawable.cine,
    "literatura" to R.drawable.literatura,
    "matematicas" to R.drawable.matematicas,
    "musica" to R.drawable.musica,
    "programacion" to R.drawable.programacion,
    "psicologia" to R.drawable.psicologia,
    "quimica" to R.drawable.quimica,
    "socrates" to R.drawable.socrates
)


val featuredGroups = listOf(
    FeaturedGroup("Humanidades", "El chiste de las ciencias políticas peruanas", "12 members", "alan", "Dominando límites, derivadas e integrales"),
    FeaturedGroup("Ciencias", "Anatomía humana", "10 members", "anatomia", "Dominando límites, derivadas e integrales"),
    FeaturedGroup("Arte", "Arte clásico y otras pinturas raritas", "7 members", "arte", "Dominando límites, derivadas e integrales"),
    FeaturedGroup("Ciencias", "Biología PUCP", "8 members", "biologia", "Dominando límites, derivadas e integrales"),
    FeaturedGroup("Arte", "Aprendiendo del cine clásico", "10 members", "cine", "Dominando límites, derivadas e integrales"),
    FeaturedGroup("Ciencias", "Literatura en París", "5 members", "literatura", "Dominando límites, derivadas e integrales")
)

val popularSubjects = listOf(
    PopularSubject("Calculus", Icons.Filled.Calculate, Color(0xFF3498DB), "2.5k"),
    PopularSubject("English Literature", Icons.Filled.MenuBook, Color(0xFFE74C3C), "1.8k"),
    PopularSubject("Physics", Icons.Filled.Science, Color(0xFF9B59B6), "1.2k"),
    PopularSubject("Chemistry", Icons.Filled.Biotech, Color(0xFF27AE60), "980"),
    PopularSubject("History", Icons.Filled.AccountBalance, Color(0xFFE67E22), "1.5k"),
    PopularSubject("Computer Science", Icons.Filled.Computer, Color(0xFF34495E), "3.2k")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(onSettingsClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "SkillShare",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C3E50),
                fontSize = 22.sp
            )
        },
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.logo), // Asegúrate que logo.png esté en res/drawable
                contentDescription = "SkillShare Logo",
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 8.dp)
                    .size(32.dp)
                    .clip(CircleShape)
            )
        },
        actions = {
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = Color(0xFF0F4C75)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFFAFAFA) // Color de fondo como en Flutter
        )
    )
}

@Composable
fun HomeContent() {
    androidx.compose.foundation.lazy.LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA)),
        contentPadding = PaddingValues(bottom = 32.dp) // Espacio al final
    ) {
        item {
            SearchBar()
        }

        item {
            SectionHeader(
                title = "Featured Groups",
                actionText = "View All",
                onActionClick = { /* TODO: Navegar a "View All" */ }
            )
        }
        item {
            FeaturedGroupsList(groups = featuredGroups)
        }

        item {
            SectionHeader(
                title = "Popular Subjects",
                actionText = "Explore All",
                onActionClick = { /* TODO: Navegar a "Explore All" */ }
            )
        }

        item {
            PopularSubjectsGrid(subjects = popularSubjects)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    var searchQuery by remember { mutableStateOf("") }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar grupos, materias...", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color(0xFF0F4C75)
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = { /* TODO: Mostrar filtros */ },
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color(0xFF0F4C75), RoundedCornerShape(8.dp))
                        .size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune, // Icono 'tune' de Flutter
                        contentDescription = "Filter",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                errorContainerColor = Color.White,

                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                cursorColor = PrimaryColor
            ),
            singleLine = true
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    actionText: String,
    onActionClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2C3E50)
        )
        TextButton(onClick = onActionClick) {
            Text(
                text = actionText,
                color = Color(0xFF0F4C75),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun FeaturedGroupsList(groups: List<FeaturedGroup>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(groups) { group ->
            FeaturedGroupCard(group = group)
        }
    }
}

@Composable
fun FeaturedGroupCard(group: FeaturedGroup) {
    Card(
        modifier = Modifier.width(280.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                Image(
                    painter = painterResource(id = imageMap[group.image] ?: R.drawable.logo),
                    contentDescription = group.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp),
                    color = Color.White.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = group.tag,
                        color = Color(0xFF2C3E50),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = group.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = group.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PeopleOutline,
                        contentDescription = "Members",
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = group.members,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun PopularSubjectsGrid(subjects: List<PopularSubject>) {
    val gridHeight = (3 * 96) + (2 * 16) + 32

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .height(gridHeight.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        userScrollEnabled = false
    ) {
        items(subjects) { subject ->
            PopularSubjectCard(subject = subject)
        }
    }
}

@Composable
fun PopularSubjectCard(subject: PopularSubject) {
    Card(
        modifier = Modifier.height(96.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(subject.color.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = subject.icon,
                        contentDescription = subject.name,
                        tint = subject.color,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = "${subject.students} students",
                    fontSize = 10.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = subject.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C3E50),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}