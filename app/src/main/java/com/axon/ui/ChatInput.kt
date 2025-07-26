package com.axon.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.axon.UiColor
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Send

@Composable
fun ChatInput(
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    var message by remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current

    val sendDisabled = message.text.trim().isEmpty() || isLoading

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(UiColor.Background.copy(alpha = 0.8f))
            .border(BorderStroke(1.dp, UiColor.Border))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .onPreviewKeyEvent { event ->
                    // TODO: Handle Enter and Shift+Enter
                    false
                },
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BasicTextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 44.dp, max = 160.dp)
                    .background(
                        UiColor.Muted.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = if (isLoading) UiColor.Border else UiColor.Primary.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                enabled = !isLoading,
                textStyle = LocalTextStyle.current.copy(color = UiColor.Foreground),
                cursorBrush = UiColor.GradientPrimary,
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (message.text.isBlank()) {
                            Text(
                                text = "Ask Axon anything...${/*if (!context.isImeGBoard()) " (Press Enter to send, Shift+Enter for new line)" else */""}",
                                color = UiColor.MutedForeground,
                                fontSize = 13.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .then(
                        if (sendDisabled) Modifier.background(UiColor.Primary.copy(alpha = 0.4f))
                        else Modifier.background(UiColor.GradientPrimary)
                    )
                    .clickable(enabled = !sendDisabled) {
                        onSendMessage(message.text.trim())
                        message = TextFieldValue("")
                    }
                    .graphicsLayer {
                        scaleX = if (sendDisabled) 1f else 1.05f
                        scaleY = if (sendDisabled) 1f else 1.05f
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp),
                        color = UiColor.PrimaryForeground
                    )
                } else {
                    Icon(
                        imageVector = Lucide.Send,
                        contentDescription = "Send",
                        tint = UiColor.PrimaryForeground,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Text(
            text = "Axon is here to help with your code. Ask questions, request explanations, or get suggestions!",
            color = UiColor.MutedForeground,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }
}
