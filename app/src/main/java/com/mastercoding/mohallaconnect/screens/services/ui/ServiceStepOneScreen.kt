package com.mastercoding.mohallaconnect.screens.services.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mastercoding.mohallaconnect.ui.theme.AccentOrange

@Composable
fun ServiceStepOneScreen(
    categories: List<OfferServiceCategory>,
    selectedServices: Set<String>,
    onServiceToggle: (String) -> Unit
) {
    Column {
        Text(
            text = "STEP 1 of 3",
            color = AccentOrange,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "What service do you offer?",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Pick the category that best matches your skill.",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        val rows = categories.chunked(3)
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            rows.forEach { rowCategories ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    rowCategories.forEach { category ->
                        val isSelected = selectedServices.contains(category.name)
                        SelectableCategoryCard(
                            category = category,
                            isSelected = isSelected,
                            modifier = Modifier.weight(1f),
                            onClick = { onServiceToggle(category.name) }
                        )
                    }
                    repeat(3 - rowCategories.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
