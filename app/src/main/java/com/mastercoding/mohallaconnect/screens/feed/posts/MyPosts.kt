package com.mastercoding.mohallaconnect.screens.feed.posts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mastercoding.mohallaconnect.data.model.User
import com.mastercoding.mohallaconnect.ui.theme.DarkBackground
import com.mastercoding.mohallaconnect.ui.theme.HeaderText
import com.mastercoding.mohallaconnect.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPosts(
    user: User?,
    onBack: () -> Unit
) {
    val viewModel: ProfileViewModel = viewModel()
    val posts by viewModel.userPosts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(user?.uid) {
        user?.let {
            viewModel.fetchUserPosts(it.uid)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Posts",
                        color = HeaderText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground
                )
            )
        },
        containerColor = DarkBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (isLoading && posts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (posts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "You haven't posted anything yet.",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(posts) { post ->
                        PostsCard(
                            post = post,
                            currentUserId = user?.uid ?: "",
                            onUpvote = {
                                user?.let { viewModel.toggleUpvote(post.id, it.uid) }
                            },
                            onDownvote = {
                                user?.let { viewModel.toggleDownvote(post.id, it.uid) }
                            }
                        )
                    }
                }
            }
        }
    }
}
