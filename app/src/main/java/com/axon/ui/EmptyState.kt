package com.axon.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.axon.UiColor
import com.composables.icons.lucide.Bot
import com.composables.icons.lucide.Code
import com.composables.icons.lucide.Lightbulb
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MessageCircle

data class Suggestion(
    val icon: ImageVector,
    val title: String,
    val description: String
)

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    val suggestions = listOf(
        Suggestion(
            Lucide.Code,
            "Code Review",
            "Help me review this function for potential issues"
        ),
        Suggestion(
            Lucide.Lightbulb,
            "Optimization",
            "How can I make this code more efficient?"
        ),
        Suggestion(
            Lucide.MessageCircle,
            "Explanation",
            "Explain how this algorithm works step by step"
        )
    )

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(bottom = 24.dp)
                .bounceAnimation()
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        brush = UiColor.GradientPrimary,
                        shape = CircleShape
                    )
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Lucide.Bot,
                    contentDescription = "Bot",
                    tint = UiColor.PrimaryForeground,
                    modifier = Modifier.size(32.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(UiColor.Success, shape = CircleShape)
                    .border(2.dp, UiColor.Border, CircleShape)
                    .align(Alignment.BottomEnd)
                    .offset(x = 4.dp, y = 4.dp)
            )
        }

        Text(
            text = "Hi! I'm Axon 👋",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "I'm your AI pair programmer, ready to help you write better code, " +
                    "debug issues, and learn new concepts. What would you like to work on today?",
            style = MaterialTheme.typography.bodyMedium,
            color = UiColor.MutedForeground,
            modifier = Modifier
                .padding(bottom = 24.dp)
                .widthIn(max = 400.dp),
            textAlign = TextAlign.Center
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 400.dp)
        ) {
            suggestions.forEach { suggestion ->
                SuggestionCard(suggestion)
            }
        }

        Text(
            text = "💡 Pro tip: You can ask me about any programming language or framework!",
            style = MaterialTheme.typography.bodySmall,
            color = UiColor.MutedForeground,
            modifier = Modifier.padding(top = 24.dp)
        )
    }
}

@Composable
fun SuggestionCard(suggestion: Suggestion) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor by animateColorAsState(
        if (isPressed) UiColor.Accent.copy(alpha = 0.1f) else UiColor.Background,
        animationSpec = tween(200)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .border(1.dp, UiColor.Border.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current
            ) {}
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isPressed) UiColor.Primary else UiColor.Muted),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = suggestion.icon,
                contentDescription = null,
                tint = if (isPressed) UiColor.PrimaryForeground else UiColor.MutedForeground,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = suggestion.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = UiColor.Foreground
            )
            Text(
                text = suggestion.description,
                style = MaterialTheme.typography.bodySmall,
                color = UiColor.MutedForeground
            )
        }
    }
}
