package com.mastercoding.mohallaconnect.screens.services

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mastercoding.mohallaconnect.ui.theme.AccentOrange
import com.mastercoding.mohallaconnect.ui.theme.HeaderText
import com.mastercoding.mohallaconnect.ui.theme.LightText

@Composable
fun ServicesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Top Icon
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color(0xFF263E4D)), // Dark teal-ish background from image
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.BusinessCenter,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = HeaderText
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Title
        Text(
            text = "Local Services",
            color = HeaderText,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Description
        Text(
            text = "We're building a network of trusted local professionals. Soon you'll find plumbers, electricians, and more right here in your neighborhood.",
            color = LightText,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Coming Soon Badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(Color(0xFFFFE8C3)) // Light orange background
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(AccentOrange)
                )
                Text(
                    text = " COMING SOON",
                    color = Color(0xFFE6A04D), // Darker orange for text
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    letterSpacing = 2.sp
                )
            }
        }
    }
}
