package com.mastercoding.mohallaconnect.screens.services.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mastercoding.mohallaconnect.ui.theme.AccentOrange
import com.mastercoding.mohallaconnect.ui.theme.DarkBackground
import com.mastercoding.mohallaconnect.ui.theme.InnerCardBackground

data class OfferServiceCategory(
    val name: String,
    val icon: ImageVector,
    val tint: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfferServiceBottomSheet(
    onDismiss: () -> Unit,
    onSubmit: (Set<String>, String, String, String, String) -> Unit = { _, _, _, _, _ -> }
) {
    var currentStep by remember { mutableIntStateOf(1) }
    var selectedServices by remember { mutableStateOf(setOf<String>()) }
    var shortDescription by remember { mutableStateOf("") }
    
    // Step 3 State
    var contactPreference by remember { mutableStateOf("WhatsApp Only") }
    var phoneNumber by remember { mutableStateOf("") }
    var whatsappNumber by remember { mutableStateOf("") }
    var selectedAvailability by remember { mutableStateOf("") }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val categories = remember {
        listOf(
            OfferServiceCategory("Plumber", Icons.Default.Build, Color(0xFF9FA8DA)),
            OfferServiceCategory("Electrician", Icons.Default.FlashOn, Color(0xFFFFCC80)),
            OfferServiceCategory("Carpenter", Icons.Default.Carpenter, Color(0xFFFFAB91)),
            OfferServiceCategory("Tutor", Icons.AutoMirrored.Filled.MenuBook, Color(0xFFA5D6A7)),
            OfferServiceCategory("HandyMan", Icons.Default.Engineering, Color(0xFF90CAF9)),
            OfferServiceCategory("Tiffin Service", Icons.Default.Restaurant, Color(0xFFF48FB1)),
            OfferServiceCategory("Maid", Icons.Default.CleaningServices, Color(0xFFB0BEC5)),
            OfferServiceCategory("More", Icons.Default.Add, Color.White)
        )
    }

    val availabilityOptions = remember {
        listOf("Weekdays", "Weekends", "Evening", "Flexible")
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = DarkBackground,
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color.Gray.copy(alpha = 0.5f)) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(550.dp)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Step Indicator Line (Uniform across all 3 steps)
            Row(
                modifier = Modifier
                    .width(120.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.Gray.copy(alpha = 0.3f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(currentStep.toFloat())
                        .background(AccentOrange)
                )
                if (currentStep < 3) {
                    Spacer(modifier = Modifier.weight((3 - currentStep).toFloat()))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Scrollable Content Area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                when (currentStep) {
                    1 -> ServiceStepOneScreen(
                        categories = categories,
                        selectedServices = selectedServices,
                        onServiceToggle = { serviceName ->
                            selectedServices = if (selectedServices.contains(serviceName)) {
                                selectedServices - serviceName
                            } else {
                                selectedServices + serviceName
                            }
                        }
                    )

                    2 -> ServiceStepTwoScreen(
                        selectedServices = selectedServices,
                        shortDescription = shortDescription,
                        onDescriptionChange = { shortDescription = it }
                    )

                    3 -> ServiceStepThreeScreen(
                        contactPreference = contactPreference,
                        onContactPreferenceChange = { contactPreference = it },
                        phoneNumber = phoneNumber,
                        onPhoneNumberChange = { phoneNumber = it },
                        whatsappNumber = whatsappNumber,
                        onWhatsappNumberChange = { whatsappNumber = it },
                        availabilityOptions = availabilityOptions,
                        selectedAvailability = selectedAvailability,
                        onAvailabilitySelect = { selectedAvailability = it }
                    )
                }
            }

            // Fixed Bottom Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Cancel/Back Button
                OutlinedButton(
                    onClick = {
                        if (currentStep == 1) {
                            onDismiss()
                        } else {
                            currentStep--
                        }
                    },
                    modifier = Modifier
                        .weight(0.35f)
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1B2E46)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = InnerCardBackground,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = if (currentStep == 1) "Cancel" else "Back",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                // Continue / Submit Button
                val isEnabled = when (currentStep) {
                    1 -> selectedServices.isNotEmpty()
                    2 -> shortDescription.isNotBlank()
                    3 -> when (contactPreference) {
                        "Phone Only" -> phoneNumber.length == 10
                        "WhatsApp Only" -> whatsappNumber.length == 10
                        else -> phoneNumber.length == 10 && whatsappNumber.length == 10
                    } && selectedAvailability.isNotEmpty()
                    else -> false
                }

                Button(
                    onClick = {
                        if (isEnabled) {
                            if (currentStep < 3) {
                                currentStep++
                            } else {
                                onSubmit(
                                    selectedServices,
                                    shortDescription,
                                    phoneNumber,
                                    whatsappNumber,
                                    selectedAvailability
                                )
                                onDismiss()
                            }
                        }
                    },
                    enabled = isEnabled,
                    modifier = Modifier
                        .weight(0.65f)
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentOrange,
                        contentColor = Color.Black,
                        disabledContainerColor = Color(0xFF102030),
                        disabledContentColor = Color(0xFF4C6075)
                    )
                ) {
                    Text(
                        text = if (currentStep == 3) "Submit ✓" else "Continue →",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SelectableCategoryCard(
    category: OfferServiceCategory,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) AccentOrange else Color.White.copy(alpha = 0.05f)
    val backgroundColor = if (isSelected) Color(0xFF1B2E46) else InnerCardBackground

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 14.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(if (isSelected) AccentOrange.copy(alpha = 0.2f) else Color(0xFF1B2E46)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.name,
                tint = if (isSelected) AccentOrange else category.tint,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = category.name,
            color = if (isSelected) AccentOrange else Color.White,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SelectableOptionCard(
    option: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) AccentOrange else Color.White.copy(alpha = 0.05f)
    val backgroundColor = if (isSelected) Color(0xFF1B2E46) else InnerCardBackground

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(backgroundColor)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 14.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = option,
                color = if (isSelected) AccentOrange else Color.White,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
            if (isSelected) {
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = AccentOrange,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
