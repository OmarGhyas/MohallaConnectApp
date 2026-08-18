package com.mastercoding.mohallaconnect.screens.feed.posts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mastercoding.mohallaconnect.data.model.Post
import com.mastercoding.mohallaconnect.ui.theme.*
import java.util.*

@Composable
fun PostsCard(
    post: Post,
    currentUserId: String,
    onUpvote: () -> Unit,
    onDownvote: () -> Unit
) {
    val upvoted = post.upvotedBy.contains(currentUserId)
    val downvoted = post.downvotedBy.contains(currentUserId)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = PostCardBackground
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF26364A)),
                    contentAlignment = Alignment.Center
                ) {
                    if (post.authorProfilePictureUrl.isNotEmpty()) {
                        AsyncImage(
                            model = post.authorProfilePictureUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        val initials = if (post.authorName.isNotEmpty()) {
                            post.authorName.take(2).uppercase()
                        } else "U"
                        Text(
                            text = initials,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = post.authorUsername,
                        color = UsernameColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = formatTimestamp(post.timestamp),
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }

                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = "More",
                    tint = Color.Gray,
                    modifier = Modifier.clickable { /* Action */ }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Content
            Text(
                text = post.content,
                color = PostTextColor,
                fontSize = 16.sp,
                lineHeight = 25.6.sp,
                fontWeight = FontWeight.Normal
            )

            // Image if present
            if (post.imageUrl.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                AsyncImage(
                    model = post.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Footer / Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Upvote Button
                    VoteButton(
                        count = post.upvotes,
                        icon = Icons.Outlined.KeyboardArrowUp,
                        isActive = upvoted,
                        activeColor = UpvoteGreen
                    ) {
                        onUpvote()
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // Separator line
                    Box(modifier = Modifier
                        .height(20.dp)
                        .width(1.dp)
                        .background(Color.DarkGray))
                    
                    Spacer(modifier = Modifier.width(8.dp))

                    // Downvote Button
                    VoteButton(
                        count = post.downvotes,
                        icon = Icons.Outlined.KeyboardArrowDown,
                        isActive = downvoted,
                        activeColor = DownvoteRed
                    ) {
                        onDownvote()
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Comment
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { /* Action */ }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ChatBubbleOutline,
                            contentDescription = "Comments",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = post.commentsCount.toString(),
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Share
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { /* Action */ }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Share",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun formatTimestamp(timestamp: com.google.firebase.Timestamp?): String {
    if (timestamp == null) return "Just now"
    val now = System.currentTimeMillis()
    val time = timestamp.toDate().time
    val diff = now - time

    return when {
        diff < 60000 -> "Just now"
        diff < 3600000 -> "${diff / 60000}m ago"
        diff < 86400000 -> "${diff / 3600000}h ago"
        else -> "${diff / 86400000}d ago"
    }
}

@Composable
fun VoteButton(
    count: Int,
    icon: ImageVector,
    isActive: Boolean,
    activeColor: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        color = if (isActive) activeColor.copy(alpha = 0.2f) else Color.Transparent
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isActive) activeColor else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = count.toString(),
                color = if (isActive) activeColor else Color.Gray,
                fontSize = 14.sp,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}
