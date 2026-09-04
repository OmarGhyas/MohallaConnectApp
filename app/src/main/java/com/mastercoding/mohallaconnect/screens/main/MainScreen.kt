package com.mastercoding.mohallaconnect.screens.main

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mastercoding.mohallaconnect.data.model.User
import com.mastercoding.mohallaconnect.navigation.MohallaDrawer
import com.mastercoding.mohallaconnect.navigation.MohallaNavHost
import com.mastercoding.mohallaconnect.navigation.Screen
import com.mastercoding.mohallaconnect.ui.theme.AccentOrange
import com.mastercoding.mohallaconnect.ui.theme.DarkBackground
import com.mastercoding.mohallaconnect.ui.theme.HeaderText
import com.mastercoding.mohallaconnect.viewmodel.AuthViewModel
import com.mastercoding.mohallaconnect.viewmodel.FeedViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(user: User?, onSignOut: () -> Unit) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val feedViewModel: FeedViewModel = viewModel()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    val blurRadius by animateDpAsState(
        targetValue = if (drawerState.targetValue == DrawerValue.Open) 10.dp else 0.dp,
        label = "BlurAnimation"
    )

    val items = listOf(
        Screen.Feed,
        Screen.Services,
        Screen.RealEstate,
        Screen.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Logic to determine current screen even if it's not in the bottom bar list
    val currentScreen = items.find { it.route == currentDestination?.route } ?: when(currentDestination?.route) {
        Screen.CreatePost.route -> Screen.CreatePost
        Screen.EditProfile.route -> Screen.EditProfile
        else -> null
    }

    val isFullScreen = currentScreen == Screen.EditProfile || 
                      currentScreen == Screen.CreatePost || 
                      currentScreen == Screen.MyPosts
    
    val topBarTitle = if (currentScreen == Screen.Feed) {
        "Mohalla Connect"
    } else {
        currentScreen?.title ?: "Mohalla Connect"
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = !isFullScreen,
        drawerContent = {
            MohallaDrawer(
                user = user,
                currentRoute = currentDestination?.route,
                onItemClick = { screen ->
                    scope.launch { drawerState.close() }
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onSettingsClick = {
                    scope.launch { drawerState.close() }
                    // TODO: Navigate to Settings
                }
            )
        }
    ) {
        Scaffold(
            modifier = Modifier.blur(blurRadius),
            topBar = {
                if (!isFullScreen) {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                topBarTitle,
                                color = Color(0xFFD98C1E),
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { 
                                scope.launch { drawerState.open() }
                            }) {
                                Icon(
                                    Icons.Default.Menu,
                                    contentDescription = "Menu",
                                    tint = Color.White
                                )
                            }
                        },
                        actions = {
                            if (currentScreen == Screen.Profile) {
                                IconButton(onClick = { navController.navigate(Screen.EditProfile.route) }) {
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
                }
            },
            bottomBar = {
                if (!isFullScreen) {
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
                }
            },
            containerColor = DarkBackground
        ) { innerPadding ->
            MohallaNavHost(
                navController = navController,
                modifier = Modifier.padding(if (isFullScreen) PaddingValues(0.dp) else innerPadding),
                user = user,
                feedViewModel = feedViewModel,
                authViewModel = authViewModel,
                onSignOut = onSignOut
            )
        }
    }
}
