package com.mastercoding.mohallaconnect.data.model

import com.google.firebase.Timestamp

data class Post(
    val id: String = "",
    val authorUid: String = "",
    val authorName: String = "",
    val authorUsername: String = "",
    val authorProfilePictureUrl: String = "",
    val content: String = "",
    val neighborhood: String = "",
    val timestamp: Timestamp? = null,
    val upvotes: Int = 0,
    val downvotes: Int = 0,
    val commentsCount: Int = 0,
    val imageUrl: String = "",
    val upvotedBy: List<String> = emptyList(),
    val downvotedBy: List<String> = emptyList()
)
