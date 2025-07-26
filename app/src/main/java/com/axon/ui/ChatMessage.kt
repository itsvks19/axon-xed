package com.axon.ui

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.axon.UiColor
import com.blankj.utilcode.util.ScreenUtils
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.Copy
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.RotateCcw
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatMessage(
    message: Message,
    isLatest: Boolean = false,
    onExplainAgain: () -> Unit = {},
) {
    if (message.role == Role.System) return

    val context = LocalContext.current
    val isUser = message.role == Role.User
    val isAssistant = message.role == Role.Assistant
    var copied by remember { mutableStateOf(false) }

    val bubbleGradient = if (isUser) UiColor.GradientUser else UiColor.GradientAi
    val bubbleForeground = if (isUser) UiColor.UserBubbleForeground else UiColor.AiBubbleForeground

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .animateContentSize(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        // Avatar and name
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (isAssistant) ConversationSide.Receiver else ConversationSide.Sender,
            modifier = Modifier.padding(bottom = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (isAssistant) ConversationSide.Receiver else ConversationSide.Sender,
                modifier = Modifier.then(if (isUser) Modifier else Modifier)
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            brush = bubbleGradient,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isUser) "Y" else "A",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = bubbleForeground
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = if (isUser) "You" else "Axon",
                    fontSize = 14.sp,
                    color = UiColor.MutedForeground
                )
            }
        }

        // Message bubble
        Column(
            modifier = Modifier
                .widthIn(max = with(LocalDensity.current) { (ScreenUtils.getScreenWidth() * 0.8f).toDp() })
                .background(
                    brush = bubbleGradient,
                    shape = messageBubbleShape(isUser)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            CompositionLocalProvider(
                LocalTextSelectionColors provides TextSelectionColors(
                    handleColor = UiColor.Foreground,
                    backgroundColor = UiColor.Foreground.copy(alpha = 0.4f)
                )
            ) {
                SelectionContainer {
                    Text(
                        text = message.content,
                        fontSize = 14.sp,
                        color = bubbleForeground
                    )
                }
            }

            Text(
                text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(message.timestamp),
                fontSize = 11.sp,
                color = bubbleForeground.copy(alpha = 0.6f),
                modifier = Modifier
                    .padding(top = 3.dp)
                    .align(if (isUser) Alignment.End else Alignment.Start)
            )
        }

        // Action Buttons
        if (isAssistant && isLatest) {
            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val clipboard = LocalClipboardManager.current

                IconTextButton(
                    onClick = {
                        clipboard.setText(AnnotatedString(message.content))
                        copied = true
                        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(2000)
                            copied = false
                        }
                    },
                    icon = if (copied) Lucide.Check else Lucide.Copy,
                    text = if (copied) "Copied!" else "Copy"
                )

//                    IconTextButton(
//                        onClick = onInsertCode,
//                        icon = Lucide.Code,
//                        text = "Insert Code"
//                    )

                IconTextButton(
                    onClick = onExplainAgain,
                    icon = Lucide.RotateCcw,
                    text = "Explain Again"
                )
            }
        }
    }
}

data class Message(
    val id: String,
    val content: String,
    val role: Role,
    val timestamp: Date
)

enum class Role {
    User, Assistant, System
}

@Composable
fun IconTextButton(
    onClick: () -> Unit,
    icon: ImageVector,
    text: String
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(12.dp),
            tint = UiColor.Foreground
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = text,
            fontSize = 12.sp,
            color = UiColor.Foreground
        )
    }
}

val messageBubbleShape: (Boolean) -> Shape = { isUser ->
    RoundedCornerShape(
        topStart = 15.dp,
        topEnd = 15.dp,
        bottomStart = if (isUser) 15.dp else 4.dp,
        bottomEnd = if (isUser) 4.dp else 15.dp
    )
}
