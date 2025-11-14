package com.skillshare.skilshare_mentor.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilePresent
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Slideshow
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skillshare.skilshare_mentor.files.colorDoc
import com.skillshare.skilshare_mentor.files.colorPdf
import com.skillshare.skilshare_mentor.files.colorPpt
import com.skillshare.skilshare_mentor.files.colorQuiz
import com.skillshare.skilshare_mentor.ui.theme.PrimaryColor
import java.lang.Float.max

private val activityData = listOf(4f, 8f, 3f, 9f, 5f, 7f, 2f)
private val activityLabels = listOf("Mo", "Tu", "We", "Th", "Fri", "Sat", "Su")

data class StatItem(
    val title: String,
    val value: String,
    val icon: ImageVector,
    val color: Color
)

private val statItems = listOf(
    StatItem("Groups Joined", "4", Icons.Default.Groups, Color(0xFF3498DB)),
    StatItem("Files Uploaded", "28", Icons.Default.FilePresent, Color(0xFF27AE60)),
    StatItem("Time Spent", "12h", Icons.Default.Timer, Color(0xFFE67E22))
)

data class FileDistribution(
    val type: String,
    val percentage: Float,
    val icon: ImageVector,
    val color: Color
)
private val fileDistData = listOf(
    FileDistribution("PDFs", 0.45f, Icons.Default.PictureAsPdf, colorPdf),
    FileDistribution("Presentations", 0.25f, Icons.Default.Slideshow, colorPpt),
    FileDistribution("Quizzes", 0.20f, Icons.Default.Quiz, colorQuiz),
    FileDistribution("Documents", 0.10f, Icons.Default.Description, colorDoc)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsContent() {
    Scaffold(
        topBar = {
            StatisticsTopBar()
        },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                StatisticsOverview()
            }

            item {
                ActivityChart()
            }

            item {
                FileDistribution()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsTopBar() {
    TopAppBar(
        title = {
            Text(
                text = "Statistics",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
fun StatisticsOverview() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        statItems.forEach { item ->
            StatCard(
                item = item,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun StatCard(item: StatItem, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .background(item.color.copy(alpha = 0.1f), CircleShape)
                    .padding(10.dp)
            ) {
                Icon(item.icon, contentDescription = item.title, tint = item.color, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                item.value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                item.title,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ActivityChart() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Weekly Activity",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            ManualBarChart(
                data = activityData,
                labels = activityLabels,
                color = PrimaryColor
            )
        }
    }
}

@Composable
fun ManualBarChart(
    data: List<Float>,
    labels: List<String>,
    color: Color,
    modifier: Modifier = Modifier,
    chartHeight: Dp = 150.dp,
    barWidth: Dp = 20.dp
) {
    val maxVal = data.maxOrNull() ?: 1f

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(chartHeight),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            data.forEach { value ->
                val barHeight = chartHeight * (value / maxVal)

                Box(
                    modifier = Modifier
                        .height(barHeight)
                        .width(barWidth)
                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                        .background(color)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            labels.forEach { label ->
                Text(
                    text = label,
                    modifier = Modifier.width(barWidth),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}


@Composable
fun FileDistribution() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "File Distribution",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                fileDistData.forEach { item ->
                    FileDistributionRow(item = item)
                }
            }
        }
    }
}

@Composable
fun FileDistributionRow(item: FileDistribution) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(item.color.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Icon(item.icon, contentDescription = item.type, tint = item.color, modifier = Modifier.size(20.dp))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            item.type,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            "${(item.percentage * 100).toInt()}%",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.width(8.dp))

        LinearProgressIndicator(
            progress = item.percentage,
            modifier = Modifier
                .width(100.dp)
                .height(8.dp)
                .clip(CircleShape),
            color = item.color
        )
    }
}