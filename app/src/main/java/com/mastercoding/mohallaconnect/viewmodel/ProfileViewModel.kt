package com.mastercoding.mohallaconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastercoding.mohallaconnect.data.model.Post
import com.mastercoding.mohallaconnect.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val repository = PostRepository()

    private val _userPosts = MutableStateFlow<List<Post>>(emptyList())
    val userPosts: StateFlow<List<Post>> = _userPosts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun fetchUserPosts(uid: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getUserPosts(uid).collect { postList ->
                    _userPosts.value = postList
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _userPosts.value = emptyList()
                _isLoading.value = false
            }
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
