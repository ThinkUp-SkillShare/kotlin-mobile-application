package com.skillshare.skilshare_mentor.home

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class FeaturedGroup(
    val tag: String,
    val name: String,
    val members: String,
    val image: String,
    val description: String
)

data class PopularSubject(
    val name: String,
    val icon: ImageVector,
    val color: Color,
    val students: String
)