package com.skillshare.skilshare_mentor.register

import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.skillshare.skilshare_mentor.ui.theme.BorderGray
import com.skillshare.skilshare_mentor.ui.theme.Gray
import com.skillshare.skilshare_mentor.ui.theme.GreenSuccess
import com.skillshare.skilshare_mentor.ui.theme.PrimaryColor
import com.skillshare.skilshare_mentor.ui.theme.White
import java.io.File
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UniversityDocumentScreen(
    onContinueClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var selectedFile by remember { mutableStateOf<FileInfo?>(null) }
    var showModal by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                val fileInfo = getFileInfoFromUri(context, it)
                selectedFile = fileInfo
            }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp)
            ) {

                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("file:///android_asset/images/common/foxdungee/university_document.png")
                        .build()
                )

                Image(
                    painter = painter,
                    contentDescription = "University Document",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp, 200.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "University document",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Attach a document that validates your teaching",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gray,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                FileUploadArea(
                    selectedFile = selectedFile,
                    onFileSelected = {
                        filePickerLauncher.launch("*/*")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                )

                if (selectedFile == null) {
                    Text(
                        text = "Supported formats: PDF, JPG, PNG, DOC, DOCX",
                        style = MaterialTheme.typography.bodySmall,
                        color = Gray,
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 40.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            if (selectedFile != null) {
                                showModal = true
                            }
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.tertiary
                        ),
                        enabled = selectedFile != null
                    ) {
                        Text(
                            text = "Continue",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Continue",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }

    if (showModal) {
        ValidationModal(
            onDismiss = { showModal = false },
            onConfirm = {
                showModal = false
                onContinueClick()
            }
        )
    }
}

@Composable
fun FileUploadArea(
    selectedFile: FileInfo?,
    onFileSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(120.dp),
        shape = RoundedCornerShape(12.dp),
        color = White,
        border = BorderStroke(
            width = 2.dp,
            color = if (selectedFile != null) GreenSuccess else BorderGray
        ),
        onClick = onFileSelected
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (selectedFile == null) {
                Icon(
                    imageVector = Icons.Default.UploadFile,
                    contentDescription = "Upload file",
                    tint = Gray,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Click on this area to load it",
                    color = Gray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val fileIcon = when {
                        selectedFile.type.contains("pdf") -> Icons.Default.PictureAsPdf
                        selectedFile.type.startsWith("image") -> Icons.Default.Image
                        else -> Icons.Default.Description
                    }

                    Icon(
                        imageVector = fileIcon,
                        contentDescription = "Document",
                        tint = PrimaryColor,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = selectedFile.name,
                            color = PrimaryColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1
                        )
                        Text(
                            text = "${selectedFile.size} • ${selectedFile.type.uppercase()}",
                            color = Gray,
                            fontSize = 12.sp
                        )
                    }

                    TextButton(
                        onClick = {  }
                    ) {
                        Text(
                            text = "Change",
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ValidationModal(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(16.dp),
            color = White
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = "Validation",
                    tint = PrimaryColor,
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Document Submitted",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "This process may take a while",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gray,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "You will receive a validation message at the email address you provided.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gray,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onConfirm
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

data class FileInfo(
    val name: String,
    val type: String,
    val size: String,
    val uri: Uri? = null
)

private fun getFileInfoFromUri(context: android.content.Context, uri: Uri): FileInfo {
    val contentResolver = context.contentResolver

    val fileName = getFileName(context, uri) ?: "unknown_file"

    val mimeType = contentResolver.getType(uri) ?: "application/octet-stream"

    val size = getFileSize(context, uri)

    return FileInfo(
        name = fileName,
        type = mimeType,
        size = formatFileSize(size),
        uri = uri
    )
}

private fun getFileName(context: android.content.Context, uri: Uri): String? {
    return when (uri.scheme) {
        "content" -> {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val displayNameIndex = cursor.getColumnIndex("_display_name")
                    if (displayNameIndex != -1) {
                        cursor.getString(displayNameIndex)
                    } else {
                        null
                    }
                } else {
                    null
                }
            }
        }
        "file" -> {
            uri.lastPathSegment
        }
        else -> null
    } ?: "document_${System.currentTimeMillis()}"
}

private fun getFileSize(context: android.content.Context, uri: Uri): Long {
    return try {
        context.contentResolver.openFileDescriptor(uri, "r")?.use { parcelFileDescriptor ->
            parcelFileDescriptor.statSize
        } ?: 0L
    } catch (e: Exception) {
        0L
    }
}

private fun formatFileSize(size: Long): String {
    return when {
        size < 1024 -> "$size B"
        size < 1024 * 1024 -> "${(size / 1024.0).roundToInt()} KB"
        else -> "${(size / (1024.0 * 1024.0)).roundToInt()} MB"
    }
}
