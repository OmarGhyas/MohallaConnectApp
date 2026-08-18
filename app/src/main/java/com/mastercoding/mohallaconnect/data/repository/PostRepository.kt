package com.mastercoding.mohallaconnect.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mastercoding.mohallaconnect.data.model.Post
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class PostRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val postsCollection = firestore.collection("posts")

    suspend fun createPost(post: Post): Result<Boolean> {
        return try {
            val docRef = postsCollection.document()
            val postWithId = post.copy(id = docRef.id)
            docRef.set(postWithId).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getPosts(): Flow<List<Post>> = callbackFlow {
        val subscription = postsCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val posts = snapshot.toObjects(Post::class.java)
                    trySend(posts)
                }
            }
        awaitClose { subscription.remove() }
    }

    suspend fun toggleUpvote(postId: String, userId: String): Result<Boolean> {
        return try {
            val postRef = postsCollection.document(postId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(postRef)
                val upvotedBy = snapshot.get("upvotedBy") as? List<String> ?: emptyList()
                val downvotedBy = snapshot.get("downvotedBy") as? List<String> ?: emptyList()

                if (upvotedBy.contains(userId)) {
                    // Remove upvote
                    transaction.update(postRef, "upvotedBy", FieldValue.arrayRemove(userId))
                    transaction.update(postRef, "upvotes", FieldValue.increment(-1))
                } else {
                    // Add upvote
                    transaction.update(postRef, "upvotedBy", FieldValue.arrayUnion(userId))
                    transaction.update(postRef, "upvotes", FieldValue.increment(1))
                    // Remove downvote if exists
                    if (downvotedBy.contains(userId)) {
                        transaction.update(postRef, "downvotedBy", FieldValue.arrayRemove(userId))
                        transaction.update(postRef, "downvotes", FieldValue.increment(-1))
                    }
                }
            }.await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleDownvote(postId: String, userId: String): Result<Boolean> {
        return try {
            val postRef = postsCollection.document(postId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(postRef)
                val upvotedBy = snapshot.get("upvotedBy") as? List<String> ?: emptyList()
                val downvotedBy = snapshot.get("downvotedBy") as? List<String> ?: emptyList()

                if (downvotedBy.contains(userId)) {
                    // Remove downvote
                    transaction.update(postRef, "downvotedBy", FieldValue.arrayRemove(userId))
                    transaction.update(postRef, "downvotes", FieldValue.increment(-1))
                } else {
                    // Add downvote
                    transaction.update(postRef, "downvotedBy", FieldValue.arrayUnion(userId))
                    transaction.update(postRef, "downvotes", FieldValue.increment(1))
                    // Remove upvote if exists
                    if (upvotedBy.contains(userId)) {
                        transaction.update(postRef, "upvotedBy", FieldValue.arrayRemove(userId))
                        transaction.update(postRef, "upvotes", FieldValue.increment(-1))
                    }
                }
            }.await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
