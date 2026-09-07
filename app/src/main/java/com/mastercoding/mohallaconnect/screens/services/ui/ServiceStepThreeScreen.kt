package com.mastercoding.mohallaconnect.screens.services.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mastercoding.mohallaconnect.ui.theme.AccentOrange
import com.mastercoding.mohallaconnect.ui.theme.InnerCardBackground

@Composable
fun ServiceStepThreeScreen(
    contactPreference: String,
    onContactPreferenceChange: (String) -> Unit,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    whatsappNumber: String,
    onWhatsappNumberChange: (String) -> Unit,
    availabilityOptions: List<String>,
    selectedAvailability: String,
    onAvailabilitySelect: (String) -> Unit
) {
    Column {
        Text(
            text = "STEP 3 of 3",
            color = AccentOrange,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Availability & contact",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Let neighbours know when you are free.",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // CONTACT PREFERENCE DROPDOWN
        Text(
            text = "HOW SHOULD NEIGHBOURS CONTACT YOU?",
            color = Color.Gray,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        ContactPreferenceDropdown(
            selectedPreference = contactPreference,
            onPreferenceSelected = onContactPreferenceChange
        )

        Spacer(modifier = Modifier.height(16.dp))

        // PHONE NUMBER FIELD (Shown for "Both" or "Phone Only")
        if (contactPreference != "WhatsApp Only") {
            Text(
                text = "PHONE NUMBER",
                color = Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            TextField(
                value = phoneNumber,
                onValueChange = { input ->
                    if (input.all { it.isDigit() } && input.length <= 10) {
                        onPhoneNumberChange(input)
                    }
                },
                placeholder = { Text("Enter 10-digit mobile number", color = Color.Gray, fontSize = 14.sp) },
                prefix = { Text("+91 ", color = Color.White, fontWeight = FontWeight.Bold) },
                trailingIcon = {
                    if (phoneNumber.length == 10) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Valid Phone Number",
                            tint = AccentOrange,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(InnerCardBackground),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // WHATSAPP NUMBER FIELD (Shown for "Both" or "WhatsApp Only")
        if (contactPreference != "Phone Only") {
            Text(
                text = "WHATSAPP NUMBER",
                color = Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            TextField(
                value = whatsappNumber,
                onValueChange = { input ->
                    if (input.all { it.isDigit() } && input.length <= 10) {
                        onWhatsappNumberChange(input)
                    }
                },
                placeholder = { Text("Enter 10-digit WhatsApp number", color = Color.Gray, fontSize = 14.sp) },
                prefix = { Text("+91 ", color = Color.White, fontWeight = FontWeight.Bold) },
                trailingIcon = {
                    if (whatsappNumber.length == 10) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Valid WhatsApp Number",
                            tint = AccentOrange,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(InnerCardBackground),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Shared only with people you accept",
                color = Color.Gray,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

        // AVAILABILITY SECTION
        Text(
            text = "WHEN ARE YOU AVAILABLE?",
            color = Color.Gray,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        val availabilityRows = availabilityOptions.chunked(2)
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            availabilityRows.forEach { rowOptions ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    rowOptions.forEach { option ->
                        val isSelected = selectedAvailability == option
                        SelectableOptionCard(
                            option = option,
                            isSelected = isSelected,
                            modifier = Modifier.weight(1f),
                            onClick = { onAvailabilitySelect(option) }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactPreferenceDropdown(
    selectedPreference: String,
    onPreferenceSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("WhatsApp Only", "Phone Only", "Phone & WhatsApp")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedPreference.ifEmpty { "WhatsApp Only" },
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(InnerCardBackground)
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(InnerCardBackground)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option, color = Color.White, fontWeight = FontWeight.Medium) },
                    onClick = {
                        onPreferenceSelected(option)
                        expanded = false
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = Color.White
                    )
                )
            }
        }
    }
}
