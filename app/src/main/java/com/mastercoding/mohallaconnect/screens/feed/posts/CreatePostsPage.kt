package com.mastercoding.mohallaconnect.screens.feed.posts

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mastercoding.mohallaconnect.data.model.User
import com.mastercoding.mohallaconnect.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostsPage(
    user: User?, 
    onPostCreated: (String, String) -> Unit,
    onBack: () -> Unit
) {
    var text by remember { mutableStateOf("") }
    val charLimit = 300
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedNeighborhood by remember { mutableStateOf("") }
    var showNeighborhoodMenu by remember { mutableStateOf(false) }

    val neighborhoods = listOf(
        "Abul Fazal Part 1", "Batla House", "Shaheen Bagh", "Zakir Nagar",
        "Ghaffar Manzil", "Johri Farm", "Jaitpur", "Madanpur Khadar"
    ).sorted()

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = InnerCardBackground
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.Gray,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Text(
                        text = "Create Post",
                        color = HeaderText,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Button(
                        onClick = { 
                            if (text.isNotEmpty() && selectedNeighborhood.isNotEmpty()) {
                                onPostCreated(text, selectedNeighborhood)
                            }
                        },
                        enabled = text.isNotEmpty() && selectedNeighborhood.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFB74D),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)
                    ) {
                        Text(text = "Post", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // User Info
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF26364A)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!user?.profilePictureUrl.isNullOrEmpty()) {
                            AsyncImage(
                                model = user.profilePictureUrl,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(32.dp),
                                tint = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = user?.username ?: "@username",
                        color = UsernameColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )

                }

                Spacer(modifier = Modifier.height(24.dp))

                // Input Field Container
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(PostCardBackground)
                        .padding(16.dp)
                ) {
                    Column {
                        TextField(
                            value = text,
                            onValueChange = {
                                if (it.length <= charLimit) {
                                    text = it
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(
                                    text = "Share an update with your neighbors...",
                                    color = Color.Gray,
                                    fontSize = 18.sp
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedTextColor = PostTextColor,
                                unfocusedTextColor = PostTextColor
                            ),
                            textStyle = TextStyle(
                                fontSize = 18.sp,
                                lineHeight = 28.sp
                            )
                        )

                        if (selectedImageUri != null) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            ) {
                                AsyncImage(
                                    model = selectedImageUri,
                                    contentDescription = "Selected Image",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                IconButton(
                                    onClick = { selectedImageUri = null },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(8.dp)
                                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = "Remove Image", tint = Color.White)
                                }
                            }
                        }
                    }

                    Text(
                        text = "${text.length}/$charLimit",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons (Attach Image & Neighborhood)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Attach Image Button
                    OutlinedButton(
                        onClick = { galleryLauncher.launch("image/*") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFF1B2E46)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = HeaderText
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Attach Image",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Neighborhood Button
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedButton(
                            onClick = { showNeighborhoodMenu = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color(0xFF1B2E46)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = HeaderText
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (selectedNeighborhood.isEmpty()) "Neighborhood" else selectedNeighborhood,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = showNeighborhoodMenu,
                            onDismissRequest = { showNeighborhoodMenu = false },
                            modifier = Modifier
                                .background(InnerCardBackground)
                                .fillMaxWidth(0.5f)
                        ) {
                            neighborhoods.forEach { area ->
                                DropdownMenuItem(
                                    text = {
                                        Text(text = area, color = PostTextColor)
                                    },
                                    onClick = {
                                        selectedNeighborhood = area
                                        showNeighborhoodMenu = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
