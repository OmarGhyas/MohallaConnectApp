package com.mastercoding.mohallaconnect.screens.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mastercoding.mohallaconnect.ui.theme.*

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreateProfileScreenPreview() {
    CreateProfileScreen(onSaveProfile = { _, _, _, _, _, _ -> })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProfileScreen(
    onSaveProfile: (String, String, String, String, String, Uri?) -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("@") }
    var email by remember { mutableStateOf("") }
    var neighbourhood by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    
    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Create Profile",
            color = HeaderText,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Introduce yourself to your new neighborhood.",
            color = Color.LightGray,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Profile Picture Section
        Box(
            modifier = Modifier.size(140.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray.copy(alpha = 0.5f), CircleShape)
                    .background(Color(0xFF0A1E33)),
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = Color.Gray
                    )
                }
            }

            // Add Button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(AccentOrange)
                    .clickable { galleryLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Photo",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Form Fields
        ProfileLabel("Full Name")
        ProfileInputField(
            value = fullName,
            onValueChange = { fullName = it },
            placeholder = "e.g. Jane Doe"
        )

        Spacer(modifier = Modifier.height(20.dp))

        ProfileLabel("Username")
        ProfileInputField(
            value = username,
            onValueChange = { input ->
                if (input.startsWith("@")) {
                    val content = input.substring(1)
                    if (content.all { it.isLetterOrDigit() || it == '_' || it == '.' } && content.length <= 15) {
                        username = input
                    }
                } else if (input.isEmpty()) {
                    username = "@"
                }
            },
            placeholder = "@ janedoe"
        )

        Spacer(modifier = Modifier.height(20.dp))

        ProfileLabel("Gmail Address")
        ProfileInputField(
            value = email,
            onValueChange = { email = it },
            placeholder = "jane@gmail.com",
            trailingIcon = {
                Icon(Icons.Default.Email, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(0.7f)) {
                ProfileLabel("Neighborhood")
                NeighbourhoodDropDown(
                    selectedNeighbourhood = neighbourhood,
                    onNeighbourhoodSelected = { neighbourhood = it }
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(0.3f)) {
                ProfileLabel("Age")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(InnerCardBackground)
                        .padding(horizontal = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    TextField(
                        value = age,
                        onValueChange = { 
                            if (it.all { char -> char.isDigit() } && it.length <= 3) {
                                age = it 
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = PostTextColor,
                            unfocusedTextColor = PostTextColor
                        ),
                        placeholder = { Text("25", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(60.dp))

        // Save Profile Button
        Button(
            onClick = {
                if (fullName.isBlank() || username == "@" || username.length < 4 || email.isBlank() || neighbourhood.isBlank() || age.isBlank()) {
                    Toast.makeText(context, "Please fill in all details", Toast.LENGTH_SHORT).show()
                }  else {
                    onSaveProfile(fullName, username, email, neighbourhood, age, imageUri)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            shape = RoundedCornerShape(32.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = HeaderText,
                contentColor = Color.Black
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Save Profile",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun ProfileLabel(text: String) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(InnerCardBackground)
            .padding(horizontal = 4.dp)
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = placeholder, color = Color.Gray, fontSize = 16.sp)
            },
            trailingIcon = trailingIcon,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = PostTextColor,
                unfocusedTextColor = PostTextColor
            ),
            singleLine = true,
            keyboardOptions = keyboardOptions
        )
    }
}
