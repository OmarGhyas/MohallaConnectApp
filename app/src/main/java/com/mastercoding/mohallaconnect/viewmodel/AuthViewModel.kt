package com.mastercoding.mohallaconnect.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastercoding.mohallaconnect.data.model.User
import com.mastercoding.mohallaconnect.data.repository.AuthRepository
import com.mastercoding.mohallaconnect.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()
    private val userRepository = UserRepository()
    
    private val _isAuthenticated = MutableStateFlow(authRepository.getCurrentUser() != null)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    private val _isProfileComplete = MutableStateFlow(false)
    val isProfileComplete: StateFlow<Boolean> = _isProfileComplete

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        checkInitialAuthState()
    }

    private fun checkInitialAuthState() {
        val firebaseUser = authRepository.getCurrentUser()
        if (firebaseUser != null) {
            _isAuthenticated.value = true
            checkProfileCompletion(firebaseUser.uid)
        }
    }

    private fun checkProfileCompletion(uid: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val exists = userRepository.checkIfProfileExists(uid)
            _isProfileComplete.value = exists
            if (exists) {
                _currentUser.value = userRepository.getUserDetails(uid)
            }
            _isLoading.value = false
        }
    }

    fun signInWithGoogle(context: Context, webClientId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = authRepository.signInWithGoogle(context, webClientId)
            result.onSuccess {
                _isAuthenticated.value = true
                val firebaseUser = authRepository.getCurrentUser()
                if (firebaseUser != null) {
                    checkProfileCompletion(firebaseUser.uid)
                }
            }.onFailure {
                _error.value = it.message ?: "Login Failed"
            }
            _isLoading.value = false
        }
    }

    fun saveProfile(
        fullName: String,
        username: String,
        email: String,
        neighbourhood: String,
        age: String,
        imageUri: Uri?
    ) {
        val uid = authRepository.getCurrentUser()?.uid ?: return
        
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            // For now, we skip image upload to Firebase Storage
            val imageUrl = ""

            val user = User(
                uid = uid,
                fullName = fullName,
                username = username,
                email = email,
                neighbourhood = neighbourhood,
                age = age.toIntOrNull() ?: 0,
                profilePictureUrl = imageUrl
            )

            val saveResult = userRepository.saveUserProfile(user)
            saveResult.onSuccess {
                _isProfileComplete.value = true
                _currentUser.value = user
            }.onFailure {
                _error.value = "Failed to save profile"
            }
            _isLoading.value = false
        }
    }

    fun onLogout() {
        authRepository.signOut()
        _isAuthenticated.value = false
        _isProfileComplete.value = false
        _currentUser.value = null
    }
}
