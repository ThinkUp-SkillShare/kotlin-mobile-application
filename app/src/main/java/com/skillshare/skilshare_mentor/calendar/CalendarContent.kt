package com.skillshare.skilshare_mentor.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skillshare.skilshare_mentor.ui.theme.PrimaryColor
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID

data class Event(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val date: LocalDate,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarContent() {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showCalendarDialog by remember { mutableStateOf(false) }
    var showEventDialog by remember { mutableStateOf(false) }

    var eventToEdit by remember { mutableStateOf<Event?>(null) }

    val allEvents = remember {
        mutableStateListOf(
            Event(
                title = "Reunión de Cálculo",
                description = "Grupo 'Cálculo II - UPC' - Repaso de integrales",
                date = LocalDate.now(),
                color = Color(0xFFE74C3C)
            ),
            Event(
                title = "Clase de Flutter",
                description = "Aplicaciones Móviles - Widgets y Estado",
                date = LocalDate.now(),
                color = Color(0xFF3498DB)
            ),
            Event(
                title = "Fundamentos de Programación",
                description = "Teoría de Objetos y Clases",
                date = LocalDate.now().plusDays(1),
                color = Color(0xFF9B59B6)
            ),
            Event(
                title = "Clase de Kotlin",
                description = "Corrutinas y Jetpack Compose",
                date = LocalDate.now().plusDays(2),
                color = Color(0xFF3498DB)
            ),
            Event(
                title = "Estudiar Angular",
                description = "Grupo 'Open Source' - Componentes",
                date = LocalDate.now().plusDays(2),
                color = Color(0xFF2ECC71)
            ),
            Event(
                title = "Quiz de Física",
                description = "Grupo 'Física I' - Dinámica",
                date = LocalDate.now().plusDays(3),
                color = Color(0xFFE67E22)
            ),
            Event(
                title = "Entrega Proyecto Final",
                description = "Subir repositorio y video demo",
                date = LocalDate.now().plusDays(4),
                color = Color(0xFFE74C3C)
            ),
            Event(
                title = "Llamada Grupal",
                description = "Coordinación con todos los grupos",
                date = LocalDate.now().plusDays(4),
                color = Color(0xFF1ABC9C)
            )
        )
    }

    val eventsForSelectedDate = allEvents.filter { it.date == selectedDate }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    )

    Scaffold(
        topBar = {
            CalendarTopBar(
                selectedDate = selectedDate,
                onShowCalendar = { showCalendarDialog = true }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    eventToEdit = null
                    showEventDialog = true
                },
                containerColor = PrimaryColor
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Evento", tint = Color.White)
            }
        }
    ) { paddingValues ->
        // Contenido Principal
        EventList(
            events = eventsForSelectedDate,
            onEventClick = { event ->
                eventToEdit = event
                showEventDialog = true
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        )
    }

    if (showCalendarDialog) {
        DatePickerDialog(
            onDismissRequest = { showCalendarDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showCalendarDialog = false
                        datePickerState.selectedDateMillis?.let { millis ->
                            selectedDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.of("UTC"))
                                .toLocalDate()
                        }
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showCalendarDialog = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showEventDialog) {
        EventEditorDialog(
            event = eventToEdit,
            onDismiss = { showEventDialog = false },
            onSave = { title, description ->
                if (eventToEdit == null) {
                    val newEvent = Event(
                        title = title,
                        description = description,
                        date = selectedDate,
                        color = getRandomColor()
                    )
                    allEvents.add(newEvent)
                } else {
                    val index = allEvents.indexOfFirst { it.id == eventToEdit!!.id }
                    if (index != -1) {
                        allEvents[index] = eventToEdit!!.copy(
                            title = title,
                            description = description
                        )
                    }
                }
                showEventDialog = false
            },
            onDelete = {
                if (eventToEdit != null) {
                    allEvents.remove(eventToEdit)
                    showEventDialog = false
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarTopBar(
    selectedDate: LocalDate,
    onShowCalendar: () -> Unit
) {
    val titleFormatter = remember { DateTimeFormatter.ofPattern("MMMM d, yyyy") }

    TopAppBar(
        title = {
            Text(
                text = titleFormatter.format(selectedDate),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            IconButton(onClick = onShowCalendar) {
                Icon(Icons.Default.CalendarToday, contentDescription = "Select Date")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}

@Composable
fun EventList(
    events: List<Event>,
    onEventClick: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    if (events.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "No hay eventos para este día.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Toca el botón + para agregar uno",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "Agenda del día:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            items(events) { event ->
                EventCard(event = event, onClick = { onEventClick(event) })
            }
        }
    }
}

@Composable
fun EventCard(event: Event, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(event.color)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = event.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = event.description,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Editar",
                tint = Color.LightGray,
                modifier = Modifier
                    .padding(16.dp)
                    .size(20.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun EventEditorDialog(
    event: Event?,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit,
    onDelete: () -> Unit
) {
    var title by remember { mutableStateOf(event?.title ?: "") }
    var description by remember { mutableStateOf(event?.description ?: "") }
    val isEditing = event != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = if (isEditing) "Editar Evento" else "Nuevo Evento")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción / Curso") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(title, description)
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text(if (isEditing) "Guardar Cambios" else "Crear")
            }
        },
        dismissButton = {
            Row {
                if (isEditing) {
                    TextButton(onClick = onDelete) {
                        Text("Eliminar", color = Color.Red)
                    }
                }
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        }
    )
}

// Función auxiliar para colores
fun getRandomColor(): Color {
    val colors = listOf(
        Color(0xFFE74C3C),
        Color(0xFF3498DB),
        Color(0xFF2ECC71),
        Color(0xFFF1C40F),
        Color(0xFF9B59B6),
        Color(0xFF1ABC9C)
    )
    return colors.random()
}