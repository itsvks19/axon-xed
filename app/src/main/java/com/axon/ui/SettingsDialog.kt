package com.axon.ui

import ai.koog.prompt.llm.LLMProvider
import ai.koog.prompt.llm.LLModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.axon.UiColor
import com.axon.availableModelsOf
import com.axon.bestModelOf
import com.axon.supportedProviders
import com.composables.icons.lucide.Bot
import com.composables.icons.lucide.Brain
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.X
import java.util.Locale

@Composable
fun SettingsDialog(
    provider: LLMProvider,
    onProviderChange: (LLMProvider) -> Unit,
    selectedModel: LLModel?,
    onModelChange: (LLModel) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .defaultMinSize(minWidth = 0.dp, minHeight = 0.dp)
                .border(BorderStroke(1.dp, UiColor.Border), RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(
                containerColor = UiColor.Card,
                contentColor = UiColor.CardForeground
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(12.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(UiColor.Card)
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .borderSide(1.dp, UiColor.Border, BorderSide.Bottom),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Settings",
                        color = UiColor.Foreground,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    IconButton(
                        onClick = onDismissRequest
                    ) {
                        Icon(
                            imageVector = Lucide.X,
                            contentDescription = "Close",
                            tint = UiColor.Foreground,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    ModelSelector(
                        provider = provider,
                        onProviderChange = onProviderChange,
                        selectedModel = selectedModel,
                        onModelChange = onModelChange
                    )
                }
            }
        }
    }
}

@Composable
fun ModelSelector(
    provider: LLMProvider,
    onProviderChange: (LLMProvider) -> Unit,
    selectedModel: LLModel?,
    onModelChange: (LLModel) -> Unit
) {
    val models = remember(provider) { availableModelsOf(provider) }
    val selectedModelData = models.find { it == selectedModel } ?: bestModelOf(provider)

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Lucide.Bot,
                contentDescription = "Bot",
                tint = UiColor.MutedForeground,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "LLM",
                color = UiColor.Foreground,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        DropdownSelector(
            items = supportedProviders,
            onItemSelect = onProviderChange,
            selectedItemName = provider.display,
            transformName = { it.display }
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Lucide.Brain,
                contentDescription = "Brain",
                tint = UiColor.MutedForeground,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "Model",
                color = UiColor.Foreground,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        DropdownSelector(
            items = models,
            onItemSelect = onModelChange,
            selectedItemName = selectedModelData.id,
            selectedItemType = null,
            transformName = { it.id }
        )
    }
}

@Composable
private fun <T> DropdownSelector(
    items: List<T>,
    onItemSelect: (T) -> Unit,
    selectedItemName: String?,
    selectedItemType: String? = null,
    transformName: (T) -> String = { it.toString() },
    transformDescription: (T) -> String? = { null },
    transformType: (T) -> String? = { null }
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, UiColor.Border),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = UiColor.Input)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedItemName ?: "Select",
                    fontSize = 14.sp,
                    color = UiColor.Foreground
                )
                selectedItemType?.let { type ->
                    Badge(type = type)
                }
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .background(UiColor.Card)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        onItemSelect(item)
                        expanded = false
                    },
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    transformName(item),
                                    color = UiColor.Foreground,
                                    fontWeight = FontWeight.Medium
                                )
                                transformDescription(item)?.let { desc ->
                                    Text(
                                        desc,
                                        fontSize = 12.sp,
                                        color = UiColor.MutedForeground
                                    )
                                }
                            }
                            transformType(item)?.let { type ->
                                Badge(type = type)
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun Badge(type: String?) {
    val (bg, fg) = when (type) {
        "fast" -> Color(0xFF22c55e) to UiColor.SecondaryForeground
        "balanced" -> Color(0xFF3b82f6) to UiColor.SecondaryForeground
        "advanced" -> Color(0xFFa855f7) to UiColor.SecondaryForeground
        else -> UiColor.Muted to UiColor.MutedForeground
    }

    Box(
        modifier = Modifier
            .background(bg.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = type?.let { type ->
                type.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.ROOT
                    ) else it.toString()
                }
            } ?: "unknown",
            fontSize = 10.sp,
            color = fg,
            fontWeight = FontWeight.Medium
        )
    }
}

