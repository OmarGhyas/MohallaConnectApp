package com.mastercoding.mohallaconnect.screens.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mastercoding.mohallaconnect.data.model.User
import com.mastercoding.mohallaconnect.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    user: User?,
    onBack: () -> Unit,
    onUpdateProfile: (String, String, String, String) -> Unit
) {
    var fullName by remember { mutableStateOf(user?.fullName ?: "") }
    var username by remember { mutableStateOf(user?.username ?: "@") }
    var neighbourhood by remember { mutableStateOf(user?.neighbourhood ?: "") }
    var age by remember { mutableStateOf(user?.age?.toString() ?: "") }
    
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile", color = Color(0xFFD98C1E)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Read-only fields
            ReadOnlyProfileField(label = "Email", value = user?.email ?: "")
            
            Spacer(modifier = Modifier.height(32.dp))
            HorizontalDivider(color = Color.Gray.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(32.dp))

            // Editable fields
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
                placeholder = "@ username"
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
                            .height(56.dp)
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

            // Update Button
            Button(
                onClick = {
                    if (fullName.isBlank() || neighbourhood.isBlank() || age.isBlank()) {
                        Toast.makeText(context, "Please fill in all details", Toast.LENGTH_SHORT).show()
                    } else {
                        onUpdateProfile(fullName, username, age, neighbourhood)
                        onBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD98C1E),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Update Profile",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
fun ReadOnlyProfileField(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        ProfileLabel(label)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(InnerCardBackground.copy(alpha = 0.5f))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(text = value, color = Color.Gray, fontSize = 16.sp)
        }
    }
}
