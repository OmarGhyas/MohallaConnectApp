package com.mastercoding.mohallaconnect

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mastercoding.mohallaconnect.screens.login.LoginScreen
import com.mastercoding.mohallaconnect.screens.main.MainScreen
import com.mastercoding.mohallaconnect.screens.profile.CreateProfileScreen
import com.mastercoding.mohallaconnect.ui.theme.MohallaConnectTheme
import com.mastercoding.mohallaconnect.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MohallaConnectTheme {
                val authViewModel: AuthViewModel = viewModel()
                val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
                val isProfileComplete by authViewModel.isProfileComplete.collectAsState()
                val currentUser by authViewModel.currentUser.collectAsState()
                val isLoading by authViewModel.isLoading.collectAsState()
                val error by authViewModel.error.collectAsState()
                val context = LocalContext.current

                // Show error if login fails
                LaunchedEffect(error) {
                    error?.let {
                        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    when {
                        !isAuthenticated -> {
                            val webClientId = getString(R.string.web_client_id)
                            LoginScreen(onGoogleSignInClick = {
                                authViewModel.signInWithGoogle(
                                    context,
                                    webClientId
                                )
                            })
                        }
                        !isProfileComplete -> {
                            CreateProfileScreen(onSaveProfile = { name, username, email, neighborhood, age, imageUri ->
                                authViewModel.saveProfile(name, username, email, neighborhood, age, imageUri)
                            })
                        }
                        else -> {
                            MainScreen(user = currentUser, onSignOut = { authViewModel.onLogout() })
                        }
                    }

                    if (isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}
