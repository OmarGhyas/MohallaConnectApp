package com.mastercoding.mohallaconnect.data.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mastercoding.mohallaconnect.data.model.User
import kotlinx.coroutines.tasks.await
import java.util.UUID

class UserRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val usersCollection = firestore.collection("users")

    suspend fun checkIfProfileExists(uid: String): Boolean {
        return try {
            val document = usersCollection.document(uid).get().await()
            document.exists()
        } catch (e: Exception) {
            false
        }
    }

    suspend fun saveUserProfile(user: User): Result<Boolean> {
        return try {
            usersCollection.document(user.uid).set(user).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadProfilePicture(uri: Uri, uid: String): Result<String> {
        return try {
            val fileName = "profile_pictures/$uid/${UUID.randomUUID()}.jpg"
            val storageRef = storage.reference.child(fileName)
            storageRef.putFile(uri).await()
            val downloadUrl = storageRef.downloadUrl.await()
            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserDetails(uid: String): User? {
        return try {
            val document = usersCollection.document(uid).get().await()
            document.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }
}
