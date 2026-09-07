package com.mastercoding.mohallaconnect.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mastercoding.mohallaconnect.data.model.User
import com.mastercoding.mohallaconnect.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: User?,
    onSignOut: () -> Unit,
    postsCount: String = "48",
    neighborsCount: String = "312",
    karmaCount: String = "1.2k"
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    
    val blurRadius by androidx.compose.animation.core.animateDpAsState(
        targetValue = if (showBottomSheet) 10.dp else 0.dp,
        label = "BlurAnimation"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
                .blur(blurRadius)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Profile Image Section
            Box(
                modifier = Modifier.size(120.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color(0xFF26364A)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = Color.Gray
                    )
                }
                // Edit badge
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(AccentOrange),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Edit Profile Picture",
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = user?.fullName ?: "",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = user?.username ?: "",
                color = Color.Gray,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Stats Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileStatItem(count = postsCount, label = "Posts")
                ProfileStatItem(count = neighborsCount, label = "Neighbors")
                ProfileStatItem(count = karmaCount, label = "UpVotes")
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Info Card Section
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = CardBackground
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    InfoRowItem(
                        icon = Icons.Outlined.Email,
                        label = "EMAIL",
                        value = user?.email ?: ""
                    )
                    HorizontalDivider(
                        color = Color(0xFF102135),
                        thickness = 1.dp
                    )
                    InfoRowItem(
                        icon = Icons.Outlined.LocationOn,
                        label = "NEIGHBORHOOD",
                        value = user?.neighbourhood ?: ""
                    )
                    HorizontalDivider(
                        color = Color(0xFF102135),
                        thickness = 1.dp
                    )
                    InfoRowItem(
                        icon = Icons.Outlined.ShoppingBag,
                        label = "AGE",
                        value = if (user?.age != null && user.age > 0) user.age.toString() else ""
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Settings Button
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { /* TODO */ },
                color = Color(0xFF1B2E46).copy(alpha = 0.5f)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Settings",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Sign Out Button
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { showBottomSheet = true },
                color = Color(0xFF2D161F) // Dark reddish/maroon for sign out
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Sign Out",
                        color = Color(0xFFFF8A80), // Light red for text
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Sign Out",
                        tint = Color(0xFFFF8A80)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                containerColor = Color(0xFF081629), // Matching themed card background
                dragHandle = { BottomSheetDefaults.DragHandle(color = Color.Gray.copy(alpha = 0.5f)) }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sign Out",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Are you sure you want to sign out?",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Button(
                        onClick = {
                            showBottomSheet = false
                            onSignOut()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFB2AD), // Light coral/red color
                            contentColor = Color(0xFF4B1010) // Dark red for text
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(text = "Sign Out", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    OutlinedButton(
                        onClick = { showBottomSheet = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1B2E46)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(text = "Cancel", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun ProfileStatItem(
    count: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = count,
            color = Color(0xFFFFA726), // Orange accent matching screenshot
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun InfoRowItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0xFF1E2D3E)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color(0xFF88A0B8),
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                color = Color(0xFF6C7D93),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

