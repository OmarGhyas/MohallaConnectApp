package com.mastercoding.mohallaconnect.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mastercoding.mohallaconnect.ui.theme.InnerCardBackground
import com.mastercoding.mohallaconnect.ui.theme.PostTextColor

@Composable
fun NeighbourhoodDropDown(
    selectedNeighbourhood: String,
    onNeighbourhoodSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val neighbourhoods = listOf(
        "Abul Fazal Part 1", "Batla House", "Shaheen Bagh", "Zakir Nagar",
        "Ghaffar Manzil", "Johri Farm", "Jaitpur", "Madanpur Khadar"
    ).sorted()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(InnerCardBackground)
            .clickable { expanded = true }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (selectedNeighbourhood.isEmpty()) "Search area" else selectedNeighbourhood,
                color = if (selectedNeighbourhood.isEmpty()) Color.Gray else PostTextColor,
                fontSize = 16.sp
            )
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .background(InnerCardBackground)
        ) {
            neighbourhoods.forEach { neighbourhood ->
                DropdownMenuItem(
                    text = {
                        Text(text = neighbourhood, color = PostTextColor)
                    },
                    onClick = {
                        onNeighbourhoodSelected(neighbourhood)
                        expanded = false
                    }
                )
            }
        }
    }
}
