package com.mastercoding.mohallaconnect.screens.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mastercoding.mohallaconnect.data.model.Post
import com.mastercoding.mohallaconnect.data.model.User
import com.mastercoding.mohallaconnect.screens.feed.posts.PostsCard
import com.mastercoding.mohallaconnect.screens.feed.posts.WhatsHappeningCard
import com.mastercoding.mohallaconnect.ui.theme.DarkBackground
import com.mastercoding.mohallaconnect.viewmodel.FeedViewModel

@Composable
fun FeedScreen(
    user: User?,
    viewModel: FeedViewModel,
    onNavigateToCreatePost: () -> Unit
) {
    val posts by viewModel.posts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // "What's Happening" placeholder card
        WhatsHappeningCard(onClick = onNavigateToCreatePost)

        if (isLoading && posts.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                androidx.compose.material3.CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
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
