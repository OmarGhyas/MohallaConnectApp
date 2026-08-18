package com.mastercoding.mohallaconnect.data.repository

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    
    fun getCurrentUser() = firebaseAuth.currentUser

    suspend fun signInWithGoogle(context: Context, webClientId: String): Result<Boolean> {
        return try {
            val credentialManager = CredentialManager.create(context)

            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(webClientId)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(context, request)
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
            val googleIdToken = googleIdTokenCredential.idToken
            
            val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
            firebaseAuth.signInWithCredential(firebaseCredential).await()
            
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }
}
