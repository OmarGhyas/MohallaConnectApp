package com.mastercoding.mohallaconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.mastercoding.mohallaconnect.data.model.Post
import com.mastercoding.mohallaconnect.data.model.User
import com.mastercoding.mohallaconnect.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FeedViewModel : ViewModel() {
    private val repository = PostRepository()

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        fetchPosts()
    }

    private fun fetchPosts() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getPosts().collect { postList ->
                _posts.value = postList
                _isLoading.value = false
            }
        }
    }

    fun createPost(content: String, neighborhood: String, user: User) {
        viewModelScope.launch {
            _isLoading.value = true
            val post = Post(
                authorUid = user.uid,
                authorName = user.fullName,
                authorUsername = user.username,
                authorProfilePictureUrl = user.profilePictureUrl,
                content = content,
                neighborhood = neighborhood,
                timestamp = Timestamp.now()
            )
            val result = repository.createPost(post)
            result.onFailure {
                _error.value = "Failed to create post"
            }
            _isLoading.value = false
        }
    }

    fun toggleUpvote(postId: String, userId: String) {
        viewModelScope.launch {
            repository.toggleUpvote(postId, userId)
        }
    }

    fun toggleDownvote(postId: String, userId: String) {
        viewModelScope.launch {
            repository.toggleDownvote(postId, userId)
        }
    }
}
