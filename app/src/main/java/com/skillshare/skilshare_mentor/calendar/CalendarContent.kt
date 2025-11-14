package com.skillshare.skilshare_mentor.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class Event(
    val id: String,
    val title: String,
    val description: String,
    val color: Color
)

private val staticEventsByDay = mapOf(
    DayOfWeek.MONDAY to listOf(
        Event("M1", "Reunión de Cálculo", "Grupo 'Cálculo II - UPC'", Color(0xFFE74C3C)),
        Event("M2", "Clase de Flutter", "Aplicaciones para dispositivos Móviles", Color(0xFF3498DB))
    ),
    DayOfWeek.TUESDAY to listOf(
        Event("T1", "Clase de Programación", "Fundamentos de Programación", Color(0xFF9B59B6))
    ),
    DayOfWeek.WEDNESDAY to listOf(
        Event("W1", "Clase de Kotlin", "Aplicaciones para dispositivos Móviles", Color(0xFF3498DB)),
        Event("W2", "Estudiar Angular", "Grupo 'Open Source'", Color(0xFF2ECC71))
    ),
    DayOfWeek.THURSDAY to listOf(
        Event("TH1", "Quiz de Física", "Grupo 'Física I'", Color(0xFFE67E22))
    ),
    DayOfWeek.FRIDAY to listOf(
        Event("F1", "Entrega Proyecto Final", "Fundamentos de Programación", Color(0xFFE74C3C)),
        Event("F2", "Llamada Grupal", "Todos los grupos", Color(0xFF1ABC9C))
    ),
    DayOfWeek.SATURDAY to emptyList(),
    DayOfWeek.SUNDAY to emptyList()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarContent() {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showCalendarDialog by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    )

    val eventsForSelectedDate by remember(selectedDate) {
        derivedStateOf {
            val dayOfWeek = selectedDate.dayOfWeek
            staticEventsByDay[dayOfWeek] ?: emptyList()
        }
    }

    Scaffold(
        topBar = {
            CalendarTopBar(
                selectedDate = selectedDate,
                onShowCalendar = { showCalendarDialog = true }
            )
        }
    ) { paddingValues ->
        EventList(
            events = eventsForSelectedDate,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        )
    }

    if (showCalendarDialog) {
        DatePickerDialog(
            onDismissRequest = {
                showCalendarDialog = false
            },
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
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCalendarDialog = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarTopBar(
    selectedDate: LocalDate,
    onShowCalendar: () -> Unit
) {
    val titleFormatter = remember {
        DateTimeFormatter.ofPattern("MMMM d, yyyy")
    }

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
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Select Date"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
fun EventList(events: List<Event>, modifier: Modifier = Modifier) {
    if (events.isEmpty()) {
        Box(
            modifier = modifier.padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "No hay eventos programados para este día.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "Eventos programados:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            items(events) { event ->
                EventCard(event)
            }
        }
    }
}

@Composable
fun EventCard(event: Event) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .heightIn(min = 60.dp)
                    .background(event.color)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = event.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = event.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}