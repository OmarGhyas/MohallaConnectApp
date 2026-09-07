package com.mastercoding.mohallaconnect.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mastercoding.mohallaconnect.data.model.User
import com.mastercoding.mohallaconnect.screens.feed.FeedScreen
import com.mastercoding.mohallaconnect.screens.feed.posts.CreatePostsPage
import com.mastercoding.mohallaconnect.screens.feed.posts.MyPosts
import com.mastercoding.mohallaconnect.screens.profile.EditProfileScreen
import com.mastercoding.mohallaconnect.screens.profile.ProfileScreen
import com.mastercoding.mohallaconnect.screens.realestate.RealEstateScreen
import com.mastercoding.mohallaconnect.screens.services.ui.ServicesScreen
import com.mastercoding.mohallaconnect.viewmodel.AuthViewModel
import com.mastercoding.mohallaconnect.viewmodel.FeedViewModel

@Composable
fun MohallaNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    user: User?,
    feedViewModel: FeedViewModel,
    authViewModel: AuthViewModel,
    onSignOut: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Feed.route,
        modifier = modifier
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
        composable(Screen.MyPosts.route) {
            MyPosts(user = user, onBack = { navController.popBackStack() })
        }
        composable(Screen.EditProfile.route) {
            EditProfileScreen(
                user = user,
                onBack = { navController.popBackStack() },
                onUpdateProfile = { name, username, age, neighborhood ->
                    authViewModel.updateProfile(name, username, age, neighborhood)
                }
            )
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
