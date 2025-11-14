package com.skillshare.skilshare_mentor.files

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skillshare.skilshare_mentor.ui.theme.PrimaryColor
import kotlinx.coroutines.launch

val colorPdf = Color(0xFFE74C3C)
val colorPpt = Color(0xFF27AE60)
val colorDoc = Color(0xFF3498DB)
val colorQuiz = Color(0xFF9B59B6)
val colorPdf2 = Color(0xFFE67E22)
val colorXls = Color(0xFF1ABC9C)
val colorDarkBlue = Color(0xFF0F4C75)
val colorTextDark = Color(0xFF2C3E50)
val colorTextGray = Color(0xFF7F8C8D)
val colorBgGray = Color(0xFFF8F9FA)

enum class FileType(val icon: ImageVector, val color: Color) {
    PDF(Icons.Filled.PictureAsPdf, colorPdf),
    PRESENTATION(Icons.Filled.Slideshow, colorPpt),
    DOCUMENT(Icons.Filled.Description, colorDoc),
    QUIZ(Icons.Filled.Quiz, colorQuiz),
    PDF_ALT(Icons.Filled.PictureAsPdf, colorPdf2),
    SPREADSHEET(Icons.Filled.TableChart, colorXls)
}

data class FileItem(
    val id: String,
    val name: String,
    val author: String,
    val date: String,
    val size: String,
    val type: FileType,
    val isFavorite: Boolean,
    val isShared: Boolean,
    val category: String
)

val allFiles = listOf(
    FileItem("1", "Cálculo Diferencial - Capítulo 3.pdf", "Dr. Martinez Rodriguez", "2 días atrás", "2.4 MB", FileType.PDF, true, false, "Matemáticas"),
    FileItem("2", "Anatomía del Sistema Nervioso.pptx", "Dra. Elena Vasquez", "1 día atrás", "8.7 MB", FileType.PRESENTATION, false, true, "Medicina"),
    FileItem("3", "Historia del Arte Clásico - Notas.docx", "Prof. Carlos Mendez", "3 días atrás", "1.2 MB", FileType.DOCUMENT, true, false, "Arte"),
    FileItem("4", "Quiz - Fundamentos de Programación.pdf", "Ing. Ana Torres", "4 días atrás", "890 KB", FileType.QUIZ, false, true, "Tecnología"),
    FileItem("5", "Filosofía Contemporánea - Resumen.pdf", "Dr. Luis Ramirez", "5 días atrás", "1.8 MB", FileType.PDF_ALT, true, false, "Humanidades"),
    FileItem("6", "Laboratorio de Química Orgánica.xlsx", "Dra. Maria Lopez", "1 semana atrás", "3.2 MB", FileType.SPREADSHEET, false, true, "Ciencias")
)

val fileCategories = listOf("Todos", "Matemáticas", "Medicina", "Arte", "Tecnología", "Humanidades", "Ciencias")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilesContent() {
    val coroutineScope = rememberCoroutineScope()

    val mainTabs = listOf("All", "Recent", "Favorites", "Shared")
    var selectedMainTabIndex by remember { mutableStateOf(0) }

    var isSearchVisible by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    var isGridView by remember { mutableStateOf(false) }

    var selectedCategory by remember { mutableStateOf("Todos") }

    var showUploadSheet by remember { mutableStateOf(false) }
    var showCreateSheet by remember { mutableStateOf(false) }
    var showFilterSheet by remember { mutableStateOf(false) }

    val filteredFiles = remember(allFiles, selectedCategory, searchQuery, selectedMainTabIndex) {
        val tabFiltered = when (mainTabs[selectedMainTabIndex]) {
            "Recientes" -> allFiles.sortedBy { it.date }
            "Favoritos" -> allFiles.filter { it.isFavorite }
            "Compartidos" -> allFiles.filter { it.isShared }
            else -> allFiles
        }

        val categoryFiltered = if (selectedCategory == "Todos") {
            tabFiltered
        } else {
            tabFiltered.filter { it.category == selectedCategory }
        }

        if (searchQuery.isBlank()) {
            categoryFiltered
        } else {
            categoryFiltered.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        it.author.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Scaffold(
        topBar = {
            FilesTopBar(
                isSearchVisible = isSearchVisible,
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onToggleSearch = { isSearchVisible = !isSearchVisible },
                isGridView = isGridView,
                onToggleView = { isGridView = !isGridView },
                onFilterClick = { showFilterSheet = true }
            )
        },
        floatingActionButton = {
            FilesFloatingActionButtons(
                onCreateClick = { showCreateSheet = true },
                onUploadClick = { showUploadSheet = true }
            )
        },
        containerColor = colorBgGray
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            TabRow(
                selectedTabIndex = selectedMainTabIndex,
                containerColor = Color.White
            ) {
                mainTabs.forEachIndexed { index, title ->
                    Tab(
                        selected = index == selectedMainTabIndex,
                        onClick = { selectedMainTabIndex = index },
                        text = { Text(title, fontSize = 14.sp) },
                        selectedContentColor = colorDarkBlue,
                        unselectedContentColor = colorTextGray
                    )
                }
            }

            FilesPage(
                files = filteredFiles,
                isGridView = isGridView,
                selectedCategory = selectedCategory,
                onCategorySelect = { selectedCategory = it }
            )
        }
    }

    if (showUploadSheet) {
        UploadBottomSheet(onDismiss = { showUploadSheet = false })
    }
    if (showCreateSheet) {
        CreateBottomSheet(onDismiss = { showCreateSheet = false })
    }
    if (showFilterSheet) {
        FilterBottomSheet(onDismiss = { showFilterSheet = false })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilesTopBar(
    isSearchVisible: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onToggleSearch: () -> Unit,
    isGridView: Boolean,
    onToggleView: () -> Unit,
    onFilterClick: () -> Unit
) {
    TopAppBar(
        title = {
            AnimatedContent(targetState = isSearchVisible, label = "TitleSearch") { isSearching ->
                if (isSearching) {
                    TextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        placeholder = { Text("Search files...", fontSize = 14.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = colorBgGray,
                            unfocusedContainerColor = colorBgGray,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true,
                        trailingIcon = {
                            IconButton(onClick = onToggleSearch) {
                                Icon(Icons.Default.Close, "Close", tint = colorDarkBlue)
                            }
                        }
                    )
                } else {
                    Text(
                        "My Files",
                        color = colorTextDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
        },
        actions = {
            AnimatedVisibility(visible = !isSearchVisible) {
                Row {
                    IconButton(onClick = onToggleSearch) {
                        Box(
                            modifier = Modifier
                                .background(colorDarkBlue.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                .padding(8.dp)
                        ) {
                            Icon(Icons.Default.Search, "Search", tint = colorDarkBlue)
                        }
                    }

                    IconButton(onClick = onToggleView) {
                        Box(
                            modifier = Modifier
                                .background(colorDarkBlue.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                .padding(8.dp)
                        ) {
                            Icon(
                                if (isGridView) Icons.Default.ViewList else Icons.Default.GridView,
                                "Change view",
                                tint = colorDarkBlue
                            )
                        }
                    }

                    IconButton(onClick = onFilterClick) {
                        Box(
                            modifier = Modifier
                                .background(colorDarkBlue.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                .padding(8.dp)
                        ) {
                            Icon(Icons.Default.Tune, "Filters", tint = colorDarkBlue)
                        }
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@Composable
fun FilesPage(
    files: List<FileItem>,
    isGridView: Boolean,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        StatCardsRow(
            totalFiles = allFiles.size,
            favorites = allFiles.count { it.isFavorite },
            shared = allFiles.count { it.isShared }
        )

        CategoryFilterChips(
            selectedCategory = selectedCategory,
            onCategorySelect = onCategorySelect
        )

        AnimatedContent(targetState = isGridView, label = "ListGridAnimated") { isGrid ->
            if (isGrid) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(files, key = { it.id }) { file ->
                        FileGridCard(file = file)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(files, key = { it.id }) { file ->
                        FileListCard(file = file)
                    }
                }
            }
        }
    }
}

@Composable
fun StatCardsRow(totalFiles: Int, favorites: Int, shared: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            title = "Total Files",
            value = totalFiles.toString(),
            icon = Icons.Default.Folder,
            color = colorDoc,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            title = "Favorites",
            value = favorites.toString(),
            icon = Icons.Default.Favorite,
            color = colorPdf,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            title = "Shared",
            value = shared.toString(),
            icon = Icons.Default.Share,
            color = colorPpt,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StatCard(title: String, value: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .background(color.copy(alpha = 0.1f), CircleShape)
                    .padding(8.dp)
            ) {
                Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = colorTextDark
            )
            Text(
                title,
                style = MaterialTheme.typography.bodySmall,
                color = colorTextGray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CategoryFilterChips(
    selectedCategory: String,
    onCategorySelect: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(fileCategories) { category ->
            val isSelected = category == selectedCategory
            FilterChip(
                selected = isSelected,
                onClick = { onCategorySelect(category) },
                label = { Text(category) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = colorDarkBlue,
                    selectedLabelColor = Color.White,
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(25.dp)
            )
        }
    }
}

@Composable
fun FileListCard(file: FileItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(file.type.color.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(file.type.icon, contentDescription = file.type.name, tint = file.type.color, modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = file.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = colorTextDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    if (file.isFavorite) {
                        Icon(Icons.Default.Favorite, "Favorito", tint = colorPdf, modifier = Modifier.size(16.dp))
                    }
                    if (file.isShared) {
                        Icon(Icons.Default.Share, "Compartido", tint = colorPpt, modifier = Modifier.size(16.dp))
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = file.author,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorTextGray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .background(file.type.color.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            file.category,
                            color = file.type.color,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(Icons.Default.AccessTime, null, tint = colorTextGray, modifier = Modifier.size(12.dp))

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(file.date, fontSize = 12.sp, color = colorTextGray, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f, fill = false))

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(file.size, fontSize = 12.sp, color = colorTextGray, fontWeight = FontWeight.Medium, maxLines = 1, softWrap = false)
                }
            }

            // Menú
            var showMenu by remember { mutableStateOf(false) }
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Default.MoreVert, "Options", tint = colorTextGray)
                }
                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(text = { Text("Open") }, onClick = { /* ... */ })
                    DropdownMenuItem(text = { Text("Download") }, onClick = { /* ... */ })
                    DropdownMenuItem(text = { Text("Share") }, onClick = { /* ... */ })
                    DropdownMenuItem(text = { Text("Delete") }, onClick = { /* ... */ })
                }
            }
        }
    }
}

@Composable
fun FileGridCard(file: FileItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(file.type.color.copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(file.type.icon, contentDescription = file.type.name, tint = file.type.color, modifier = Modifier.size(20.dp))
                }
                Row {
                    if (file.isFavorite) {
                        Icon(Icons.Default.Favorite, "Favorito", tint = colorPdf, modifier = Modifier.size(14.dp))
                    }
                    if (file.isShared) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Default.Share, "Compartido", tint = colorPpt, modifier = Modifier.size(14.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = file.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = colorTextDark,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = file.author,
                style = MaterialTheme.typography.bodySmall,
                color = colorTextGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .background(file.type.color.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    file.category,
                    color = file.type.color,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(file.size, fontSize = 10.sp, color = colorTextGray, fontWeight = FontWeight.Medium)
                Text(file.date, fontSize = 10.sp, color = colorTextGray)
            }
        }
    }
}

@Composable
fun FilesFloatingActionButtons(
    onCreateClick: () -> Unit,
    onUploadClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FloatingActionButton(
            onClick = onCreateClick,
            containerColor = colorDoc,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(Icons.Default.NoteAdd, "Create note")
        }

        ExtendedFloatingActionButton(
            onClick = onUploadClick,
            containerColor = colorDarkBlue,
            contentColor = Color.White,
            text = { Text("Upload file", fontWeight = FontWeight.SemiBold) },
            icon = { Icon(Icons.Default.CloudUpload, "Upload file") }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadBottomSheet(onDismiss: () -> Unit) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Upload File", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                UploadOption("From device", Icons.Default.PhoneAndroid, colorDoc, Modifier.weight(1f))
                UploadOption("From cloud", Icons.Default.Cloud, colorPpt, Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                UploadOption("Take photo", Icons.Default.CameraAlt, colorPdf, Modifier.weight(1f))
                UploadOption("Scan", Icons.Default.DocumentScanner, colorQuiz, Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun UploadOption(text: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(color.copy(alpha = 0.1f))
            .clickable { /* TODO */ }
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(color, CircleShape)
                .padding(12.dp)
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(text, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = color, textAlign = TextAlign.Center)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBottomSheet(onDismiss: () -> Unit) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Create Content", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(24.dp))
            CreateOption("Create note", Icons.Default.NoteAdd, colorDoc)
            Spacer(modifier = Modifier.height(16.dp))
            CreateOption("Create quiz", Icons.Default.Quiz, colorQuiz)
            Spacer(modifier = Modifier.height(16.dp))
            CreateOption("Create presentation", Icons.Default.Slideshow, colorPpt)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CreateOption(text: String, icon: ImageVector, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.1f))
            .clickable { /* TODO */ }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(color, RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = color)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(onDismiss: () -> Unit) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
        ) {
            Text("Filters", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(24.dp))

            Text("File type", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = false, onClick = { /*TODO*/ }, label = { Text("PDF") })
                FilterChip(selected = false, onClick = { /*TODO*/ }, label = { Text("DOC") })
                FilterChip(selected = false, onClick = { /*TODO*/ }, label = { Text("PPT") })
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Upload date", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = false, onClick = { /*TODO*/ }, label = { Text("Today") })
                FilterChip(selected = false, onClick = { /*TODO*/ }, label = { Text("This week") })
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                    Text("Clear")
                }
                Button(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                    Text("Apply")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}