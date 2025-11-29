package com.skillshare.skilshare_mentor.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.skillshare.skilshare_mentor.ui.theme.PrimaryColor

data class MenuOption(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val destination: DashboardTab,
    val description: String
)

@Composable
fun HomeContent(
    onNavigate: (DashboardTab) -> Unit,
    onSettingsClick: () -> Unit,
    onEditProfileClick: () -> Unit
) {
    val menuOptions = listOf(
        /*
        MenuOption("Mis Grupos", Icons.Default.Groups, Color(0xFF5E35B1), DashboardTab.Groups, "Gestiona tus clases"),
        MenuOption("Archivos", Icons.Default.Folder, Color(0xFFFB8C00), DashboardTab.Files, "Material de estudio"),
        */
        MenuOption("Calendario", Icons.Default.CalendarMonth, Color(0xFF43A047), DashboardTab.Calendar, "Agenda académica"),
        MenuOption("Estadísticas", Icons.Default.Analytics, Color(0xFFE53935), DashboardTab.Statistics, "Rendimiento"),
        MenuOption("Mi Perfil", Icons.Default.Person, Color(0xFF1E88E5), DashboardTab.Profile, "Datos personales")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
    ) {
        HomeHeader(onProfileClick = onEditProfileClick)

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(menuOptions) { option ->
                MenuCard(option = option, onClick = { onNavigate(option.destination) })
            }
        }
    }
}

@Composable
fun HomeHeader(onProfileClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(PrimaryColor, PrimaryColor.copy(alpha = 0.8f))
                ),
            )
            .padding(top = 48.dp, bottom = 32.dp, start = 24.dp, end = 24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Bienvenido,",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Docente Sebastián",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            IconButton(
                onClick = onProfileClick,
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.White.copy(alpha = 0.2f), CircleShape)
                    .padding(3.dp)
            ) {
                AsyncImage(
                    model = "https://images5.alphacoders.com/107/1070324.jpg",
                    contentDescription = "Editar Perfil",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun MenuCard(option: MenuOption, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(option.color.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = option.icon,
                    contentDescription = null,
                    tint = option.color,
                    modifier = Modifier.size(28.dp)
                )
            }

            Column {
                Text(
                    text = option.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = option.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}