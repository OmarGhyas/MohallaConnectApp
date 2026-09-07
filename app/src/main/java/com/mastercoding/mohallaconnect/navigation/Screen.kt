package com.mastercoding.mohallaconnect.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Feed : Screen("feed", "Feed", Icons.Default.Home)
    object Services : Screen("services", "Services", Icons.Default.BusinessCenter)
    object RealEstate : Screen("real_estate", "Real Estate", Icons.Default.RealEstateAgent)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
    object CreatePost : Screen("create_post", "Create Post", Icons.Default.Home)
    object EditProfile : Screen("edit_profile", "Edit Profile", Icons.Default.Edit)
    object MyPosts : Screen("my_posts", "My Posts", Icons.Default.GridView)
}
