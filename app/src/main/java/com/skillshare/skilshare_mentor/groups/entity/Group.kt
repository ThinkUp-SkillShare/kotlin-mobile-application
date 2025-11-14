package com.skillshare.skilshare_mentor.groups.entity

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import com.skillshare.skilshare_mentor.ui.theme.*


data class Group(
    val id: String,
    val name: String,
    val description: String,
    val category: GroupCategory,
    val memberCount: Int,
    val lastActive: String,
    val createdDate: String,
    val imageUrl: String? = null
)

enum class GroupCategory(
    val displayName: String,
    val color: Color
) {
    PROGRAMMING(
        displayName = "Programación",
        color = PrimaryColor
    ),
    MATHEMATICS(
        displayName = "Matemáticas",
        color = SecondaryColor
    ),
    SCIENCE(
        displayName = "Ciencias",
        color = SecondaryColor
    ),
    ARTS(
        displayName = "Artes",
        color = SecondaryColor
    ),
    LITERATURE(
        displayName = "Literatura",
        color = SecondaryColor
    ),
    SOCIAL_SCIENCES(
        displayName = "Ciencias Sociales",
        color = SecondaryColor
    ),
    BUSINESS(
        displayName = "Negocios",
        color = SecondaryColor
    ),
    LANGUAGE(
        displayName = "Idiomas",
        color = SecondaryColor
    )
}