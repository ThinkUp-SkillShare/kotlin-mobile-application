package com.skillshare.skilshare_mentor.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.res.stringResource
import com.skillshare.skilshare_mentor.R

data class UserProfile(
    val id: String,
    val firstName: String,
    val lastName: String,
    val institution: String,
    val joinDate: String,
    val profileImageUrl: String,
    val stats: UserStats,
    val badges: List<Badge>,
    val createdGroups: List<GroupSummary>,
    val reviews: List<Review>
)

data class UserStats(
    val groupsCount: Int,
    val postsCount: Int,
    val studentsCount: Int
)

data class Badge(
    val name: String,
    val icon: ImageVector,
    val color: Color,
    val description: String
)

data class GroupSummary(
    val id: String,
    val name: String,
    val coverUrl: String?
)

data class Review(
    val id: String,
    val username: String,
    val rating: Int,
    val comment: String,
    val date: String
)

val mockUserProfile = UserProfile(
    id = "1",
    firstName = "Sebastian",
    lastName = "Ramirez",
    institution = "Universidad Peruana de Ciencias Aplicadas (UPC)",
    joinDate = "Miembro desde Enero 2025",
    profileImageUrl = "https://images5.alphacoders.com/107/1070324.jpg",
    stats = UserStats(15, 9, 22),
    badges = listOf(
        Badge("Mentor", Icons.Default.People, Color(0xFFFF9800), "Outstanding"),
        Badge("Estudiante", Icons.Default.School, Color(0xFF2196F3), "Destacado"),
        Badge("Líder", Icons.Default.VerifiedUser, Color(0xFF4CAF50), "Verificado"),
        Badge("Explorador", Icons.Default.Explore, Color(0xFF9C27B0), "Curioso")
    ),
    createdGroups = listOf(
        GroupSummary("1", "Fundamentos de la Programación", null),
        GroupSummary("2", "Cálculo II - Avanzado", null),
        GroupSummary("3", "Aprendiendo del cine clásico", null),
        GroupSummary("4", "Literatura Francesa", null)
    ),
    reviews = listOf(
        Review("1", "Carlos M.", 5, "Excelente mentor, explica muy claro los conceptos difíciles.", "Hace 2 días"),
        Review("2", "Ana T.", 5, "El grupo de estudio me ayudó a pasar el parcial. ¡Gracias!", "Hace 1 semana")
    )
)

@Composable
fun ProfileContent(
    firstName: String,
    lastName: String,
    institution: String
) {
    val user = mockUserProfile.copy(
        firstName = firstName,
        lastName = lastName,
        institution = institution
    )
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 20.dp)
    ) {
        ProfileInfoSection(user)

        Spacer(modifier = Modifier.height(24.dp))

        ProfileStatsSection(user.stats)

        Spacer(modifier = Modifier.height(32.dp))

        BadgesSection(user.badges)

        Spacer(modifier = Modifier.height(32.dp))

        GroupsCreatedSection(user.createdGroups)

        Spacer(modifier = Modifier.height(32.dp))

        ReviewsSection(user.reviews)
    }
}

@Composable
fun ProfileInfoSection(user: UserProfile) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .shadow(elevation = 8.dp, shape = CircleShape)
                .background(MaterialTheme.colorScheme.surface, CircleShape)
                .padding(4.dp)
        ) {
            AsyncImage(
                model = user.profileImageUrl,
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "${user.firstName} ${user.lastName}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = user.institution,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@Composable
fun ProfileStatsSection(stats: UserStats) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        StatCard(number = stats.groupsCount.toString(), label = stringResource(R.string.stats_groups), modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(12.dp))
        StatCard(number = stats.postsCount.toString(), label = stringResource(R.string.profile_posts), modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(12.dp))
        StatCard(number = stats.studentsCount.toString(), label = stringResource(R.string.stats_students), modifier = Modifier.weight(1f))
    }
}

@Composable
fun StatCard(number: String, label: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(85.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = number,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun BadgesSection(badges: List<Badge>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionTitle(stringResource(R.string.profile_badges))

        if (badges.isEmpty()) {
            EmptyStateText(stringResource(R.string.profile_empty_badges))
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(badges) { badge ->
                    BadgeCard(badge = badge)
                }
            }
        }
    }
}

@Composable
fun BadgeCard(badge: Badge) {
    Card(
        modifier = Modifier.size(110.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = badge.icon,
                contentDescription = null,
                tint = badge.color,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = badge.name,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = badge.description,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                fontSize = 10.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun GroupsCreatedSection(groups: List<GroupSummary>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionTitle(stringResource(R.string.profile_created_groups))

        if (groups.isEmpty()) {
            EmptyStateText(stringResource(R.string.profile_empty_groups))
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(groups) { group ->
                    GroupCard(group = group)
                }
            }
        }
    }
}

@Composable
fun GroupCard(group: GroupSummary) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(160.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                if (group.coverUrl != null) {
                    AsyncImage(
                        model = group.coverUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Groups,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = group.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    minLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun ReviewsSection(reviews: List<Review>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionTitle(stringResource(R.string.profile_reviews))

        if (reviews.isEmpty()) {
            EmptyStateText(stringResource(R.string.profile_empty_reviews))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                reviews.forEach { review ->
                    ReviewCard(review = review)
                }

                OutlinedButton(
                    onClick = { /* Cargar más */ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(stringResource(R.string.profile_view_reviews))
                }
            }
        }
    }
}

@Composable
fun ReviewCard(review: Review) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = review.username.first().toString(),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = review.username,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = review.date,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
                Row {
                    repeat(5) { index ->
                        Icon(
                            imageVector = if (index < review.rating) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = null,
                            tint = Color(0xFFFFC107), // Dorado siempre
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = review.comment,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(start = 20.dp, bottom = 12.dp)
    )
}

@Composable
fun EmptyStateText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
        modifier = Modifier.padding(horizontal = 20.dp)
    )
}