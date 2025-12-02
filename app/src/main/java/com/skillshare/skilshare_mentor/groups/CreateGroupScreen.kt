package com.skillshare.skilshare_mentor.groups

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.skillshare.skilshare_mentor.R
import com.skillshare.skilshare_mentor.ui.theme.PrimaryColor

@Composable
fun CreateGroupScreen(
    onCreateClick: (name: String, subject: String, topic: String, description: String, privacy: String) -> Unit
) {
    var groupName by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var topic by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var privacy by remember { mutableStateOf("Public") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CreateGroupTextField(
            value = groupName,
            onValueChange = { groupName = it },
            label = stringResource(R.string.group_name)
        )
        CreateGroupTextField(
            value = subject,
            onValueChange = { subject = it },
            label = stringResource(R.string.group_subject)
        )
        CreateGroupTextField(
            value = topic,
            onValueChange = { topic = it },
            label = stringResource(R.string.group_topic)
        )
        CreateGroupTextField(
            value = description,
            onValueChange = { description = it },
            label = stringResource(R.string.group_desc),
            minLines = 4
        )

        Text(
            text = stringResource(R.string.privacy),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 8.dp)
        )

        PrivacyOptionRow(
            text = stringResource(R.string.privacy_public),
            description = stringResource(R.string.privacy_public_desc),
            selected = privacy == "Public",
            onClick = { privacy = "Public" }
        )

        PrivacyOptionRow(
            text = stringResource(R.string.privacy_private),
            description = stringResource(R.string.privacy_private_desc),
            selected = privacy == "Private",
            onClick = { privacy = "Private" }
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                onCreateClick(groupName, subject, topic, description, privacy)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = groupName.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
        ) {
            Text(stringResource(R.string.create_group_btn))
        }
    }
}

@Composable
private fun CreateGroupTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        minLines = minLines,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryColor,
            focusedLabelColor = PrimaryColor,
            cursorColor = PrimaryColor,
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

@Composable
private fun PrivacyOptionRow(
    text: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = PrimaryColor)
        )
    }
}