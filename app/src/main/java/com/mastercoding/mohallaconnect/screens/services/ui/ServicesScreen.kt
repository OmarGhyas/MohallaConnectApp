package com.mastercoding.mohallaconnect.screens.services.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mastercoding.mohallaconnect.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var showOfferServiceSheet by remember { mutableStateOf(false) }

    val blurRadius by animateDpAsState(
        targetValue = if (showOfferServiceSheet) 10.dp else 0.dp,
        label = "BlurAnimation"
    )

    val categories = listOf(
        ServiceCategory("Plumber", Icons.Default.Build, Color(0xFF9FA8DA)),
        ServiceCategory("Electrician", Icons.Default.FlashOn, Color(0xFFFFCC80)),
        ServiceCategory("Carpenter", Icons.Default.Carpenter, Color(0xFFFFAB91)),
        ServiceCategory("Tutor", Icons.AutoMirrored.Filled.MenuBook, Color(0xFFA5D6A7)),
        ServiceCategory("HandyMan", Icons.Default.Engineering, Color(0xFF90CAF9)),
        ServiceCategory("Tiffin Service", Icons.Default.Restaurant, Color(0xFFF48FB1)),
        ServiceCategory("Maid", Icons.Default.CleaningServices, Color(0xFFB0BEC5)),
        ServiceCategory("More", Icons.Default.Add, Color.White)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .blur(blurRadius)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Trusted services from people around your neighbourhood.",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Search Bar
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search Plumber, Tutor, Electrician...", color = Color.Gray, fontSize = 14.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AccentOrange, modifier = Modifier.size(20.dp)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(InnerCardBackground),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Browse Services Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Browse services",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "VIEW ALL",
                color = AccentOrange,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { /* Action */ }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Services Grid (2x4)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.take(4).forEach { category ->
                    ServiceCategoryCard(
                        title = category.name,
                        icon = category.icon,
                        iconTint = category.tint,
                        modifier = Modifier.weight(1f),
                        onClick = { /* Action */ }
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.drop(4).forEach { category ->
                    ServiceCategoryCard(
                        title = category.name,
                        icon = category.icon,
                        iconTint = category.tint,
                        modifier = Modifier.weight(1f),
                        onClick = { /* Action */ }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Recommended Nearby Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recommended nearby",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

        }

        // Empty Recommended Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Coming soon...", color = Color.Gray, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action Cards
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActionCard(
                title = "Request a service",
                subtitle = "Tell your mohalla what you need.",
                icon = Icons.Default.Add,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                onClick = { /* Request service action */ }
            )
            ActionCard(
                title = "Offer a service",
                subtitle = "Share your skills locally.",
                icon = Icons.Default.Diamond,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                onClick = { showOfferServiceSheet = true }
            )
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Text(
            text = "Your selected mohalla · services shown within 3 km",
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(40.dp))
    }

    if (showOfferServiceSheet) {
        OfferServiceBottomSheet(
            onDismiss = { showOfferServiceSheet = false },
            onSubmit = { services, description, phone, whatsapp, availability ->
                showOfferServiceSheet = false
                // Service registration submitted successfully
            }
        )
    }
}

@Composable
fun ActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(InnerCardBackground)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AccentOrange,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = title,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subtitle,
            color = Color.Gray,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
    }
}

data class ServiceCategory(
    val name: String,
    val icon: ImageVector,
    val tint: Color
)
