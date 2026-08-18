package com.mastercoding.mohallaconnect.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mastercoding.mohallaconnect.data.model.User
import com.mastercoding.mohallaconnect.screens.feed.FeedScreen
import com.mastercoding.mohallaconnect.screens.feed.posts.CreatePostsPage
import com.mastercoding.mohallaconnect.screens.profile.ProfileScreen
import com.mastercoding.mohallaconnect.screens.realestate.RealEstateScreen
import com.mastercoding.mohallaconnect.screens.services.ServicesScreen
import com.mastercoding.mohallaconnect.ui.theme.AccentOrange
import com.mastercoding.mohallaconnect.ui.theme.DarkBackground
import com.mastercoding.mohallaconnect.ui.theme.HeaderText
import com.mastercoding.mohallaconnect.viewmodel.FeedViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Feed : Screen("feed", "Feed", Icons.Default.Home)
    object Services : Screen("services", "Services", Icons.Default.BusinessCenter)
    object RealEstate : Screen("real_estate", "Real Estate", Icons.Default.RealEstateAgent)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
    object CreatePost : Screen("create_post", "Create Post", Icons.Default.Home)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(user: User?, onSignOut: () -> Unit) {
    val navController = rememberNavController()
    val feedViewModel: FeedViewModel = viewModel()
    val items = listOf(
        Screen.Feed,
        Screen.Services,
        Screen.RealEstate,
        Screen.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val currentScreen = items.find { it.route == currentDestination?.route }
    val topBarTitle = if (currentScreen == Screen.Feed) {
        "Mohalla Connect"
    } else {
        currentScreen?.title ?: "Mohalla Connect"
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        topBarTitle,
                        color = HeaderText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    if (currentScreen == Screen.Profile) {
                        IconButton(onClick = { /* TODO: Edit Profile */ }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = DarkBackground
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = DarkBackground,
                tonalElevation = 8.dp
            ) {
                items.forEach { screen ->
                    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.title, fontSize = 10.sp) },
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            unselectedIconColor = Color.Gray,
                            selectedTextColor = Color.White,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = AccentOrange
                        )
                    )
                }
            }
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Feed.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Feed.route) { 
                FeedScreen(
                    user = user,
                    viewModel = feedViewModel,
                    onNavigateToCreatePost = { navController.navigate(Screen.CreatePost.route) }
                ) 
            }
            composable(Screen.Services.route) { ServicesScreen() }
            composable(Screen.RealEstate.route) { RealEstateScreen() }
            composable(Screen.Profile.route) { 
                ProfileScreen(user = user, onSignOut = onSignOut)
            }
            composable(Screen.CreatePost.route) { 
                CreatePostsPage(
                    user = user, 
                    onPostCreated = { content, neighborhood ->
                        if (user != null) {
                            feedViewModel.createPost(content, neighborhood, user)
                        }
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
