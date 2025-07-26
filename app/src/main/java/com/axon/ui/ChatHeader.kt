package com.axon.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.axon.UiColor
import com.composables.icons.lucide.Bot
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Settings2

@Composable
fun ChatHeader(
    onSettingsClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(UiColor.Background)
            .border(BorderStroke(1.dp, UiColor.Border))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(32.dp)) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            brush = UiColor.GradientPrimary,
                            shape = CircleShape
                        )
                        .align(Alignment.Center)
                ) {
                    Icon(
                        imageVector = Lucide.Bot,
                        contentDescription = "Bot",
                        tint = UiColor.PrimaryForeground,
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.Center)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .align(Alignment.TopEnd)
                        .background(UiColor.Success, CircleShape)
                        .border(2.dp, UiColor.Border, CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    "Axon", style = MaterialTheme.typography.titleMedium.copy(
                        color = UiColor.Foreground
                    )
                )
                Text(
                    "Your AI pair programmer",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = UiColor.MutedForeground
                    )
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {

            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Lucide.Settings2,
                    contentDescription = "Settings",
                    tint = UiColor.Foreground,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
