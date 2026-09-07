package com.mastercoding.mohallaconnect.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mastercoding.mohallaconnect.data.model.User
import com.mastercoding.mohallaconnect.ui.theme.AccentOrange
import com.mastercoding.mohallaconnect.ui.theme.DarkBackground

@Composable
fun MohallaDrawer(
    user: User?,
    currentRoute: String?,
    onItemClick: (Screen) -> Unit,
    onSettingsClick: () -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = DarkBackground,
        drawerShape = RoundedCornerShape(0.dp),
        modifier = Modifier.fillMaxHeight().width(320.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Header: User Profile Area
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color(0xFF00BCD4), CircleShape) // Teal border
                        .background(Color(0xFF1B2E46)),
                    contentAlignment = Alignment.Center
                ) {
                    if (!user?.profilePictureUrl.isNullOrEmpty()) {
                        AsyncImage(
                            model = user?.profilePictureUrl,
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
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Text(
                    text = user?.fullName ?: "Priya Patel",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = Color.Gray.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(24.dp))

            // Navigation Items
            val items = listOf(
                DrawerItem("Profile", Icons.Outlined.Person, Screen.Profile),
                DrawerItem("My Posts", Icons.Outlined.GridView, Screen.MyPosts),
                DrawerItem("Feed", Icons.Outlined.Home, Screen.Feed),
                DrawerItem("Services", Icons.Outlined.BusinessCenter, Screen.Services),
                DrawerItem("Real Estate", Icons.Outlined.RealEstateAgent, Screen.RealEstate)
            )

            items.forEach { item ->
                val isSelected = currentRoute == item.screen.route
                
                NavigationDrawerItem(
                    label = { 
                        Text(
                            text = item.title,
                            fontSize = 18.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        ) 
                    },
                    selected = isSelected,
                    onClick = { onItemClick(item.screen) },
                    icon = { 
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null
                        ) 
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.Transparent,
                        selectedContainerColor = AccentOrange,
                        selectedIconColor = Color.Black,
                        selectedTextColor = Color.Black,
                        unselectedIconColor = Color.White,
                        unselectedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .height(56.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Footer: Settings
            HorizontalDivider(color = Color.Gray.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSettingsClick() }
                    .padding(vertical = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Settings",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

data class DrawerItem(
    val title: String,
    val icon: ImageVector,
    val screen: Screen
)
