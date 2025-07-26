package com.axon.ui

import ai.koog.agents.core.agent.config.AIAgentConfig
import ai.koog.prompt.dsl.Prompt
import ai.koog.prompt.llm.LLMProvider
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.axon.SYSTEM_PROMPT
import com.axon.UiColor
import com.axon.apiKey
import com.axon.availableModelsOf
import com.axon.bestModelOf
import com.axon.createAgent
import com.axon.lastLLMProvider
import com.axon.lastLLModel
import com.axon.requiresApiKey
import com.axon.supportedProviders
import com.blankj.utilcode.util.ScreenUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

@Composable
fun Axon(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()

    val messages = remember { mutableStateListOf<Message>() }
    var isLoading by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }
    var agentStatus by remember { mutableStateOf("Thinking...") }
    var provider by remember {
        mutableStateOf(supportedProviders.find { it.id == lastLLMProvider } ?: LLMProvider.Google)
    }
    var selectedModel by remember(provider) {
        mutableStateOf(
            availableModelsOf(provider).find { it.id == lastLLModel } ?: bestModelOf(provider)
        )
    }
    val listState = rememberLazyListState()

//    val agent by remember { derivedStateOf { createAgent(provider, selectedModel) } }

    LaunchedEffect(messages.size) {
        listState.animateScrollToItem(listState.layoutInfo.totalItemsCount)
    }

    suspend fun handleSendMessage(message: String, inculde: Boolean = true) =
        withContext(Dispatchers.IO) {
            if (inculde) {
                messages += Message(generateId(), message, Role.User, Date())
            }

            isLoading = true
            agentStatus = "Thinking..."

            if (provider.requiresApiKey() && apiKey.isBlank()) {
                messages += Message(
                    id = generateId(),
                    content = "API key not found. Please set your API key in the settings.",
                    role = Role.Assistant,
                    timestamp = Date()
                )
                isLoading = false
                return@withContext
            }

            val config = AIAgentConfig(
                prompt = Prompt.build("axon") {
                    system(SYSTEM_PROMPT.trim())
                    for ((_, message, role) in messages) {
                        if (role == Role.Assistant) {
                            assistant(message)
                        } else if (role == Role.User) {
                            user(message)
                        }
                    }
                },
                model = selectedModel,
                maxAgentIterations = 10
            )

            val agent = createAgent(provider, config)
            val result = runCatching { agent.runAndGetResult(message) }.getOrElse {
                it.printStackTrace()
                "Something went wrong.\n\n${it.message}"
            }

            println(result)

            messages += Message(
                id = generateId(),
                content = result.toString(),
                role = Role.Assistant,
                timestamp = Date()
            )
            isLoading = false
            agentStatus = ""
        }

    Surface(
        modifier = modifier,
        color = UiColor.Background,
        contentColor = UiColor.Foreground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .borderSide(
                    strokeWidth = 1.dp,
                    color = UiColor.Border,
                    side = BorderSide.Left
                )
                .borderSide(
                    strokeWidth = 1.dp,
                    color = UiColor.Border,
                    side = BorderSide.Right
                )
        ) {
            ChatHeader(onSettingsClick = { showSettings = !showSettings })

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (messages.none { it.role != Role.System }) {
                    EmptyState(modifier = Modifier.fillMaxSize())
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        contentPadding = PaddingValues(vertical = 4.dp)
                    ) {
                        items(messages.filter { it.role != Role.System }) { message ->
                            ChatMessage(
                                message = message,
                                isLatest = message == messages.lastOrNull { it.role != Role.System },
                                onExplainAgain = {
                                    messages.remove(message)
                                    scope.launch { handleSendMessage(message.content, false) }
                                }
                            )
                        }

                        if (isLoading) {
                            item {
                                LoadingBubble(agentStatus)
                            }
                        }
                    }
                }
            }

            ChatInput(
                onSendMessage = { user ->
                    scope.launch { handleSendMessage(user) }
                },
                isLoading = isLoading
            )
        }

        if (showSettings) {
            SettingsDialog(
                provider = provider,
                onProviderChange = {
                    provider = it
                    lastLLMProvider = it.id
                },
                selectedModel = selectedModel,
                onModelChange = {
                    selectedModel = it
                    lastLLModel = it.id
                },
                onDismissRequest = { showSettings = false }
            )
        }
    }
}

private fun generateId() = "${System.currentTimeMillis()}"

@Composable
fun LoadingBubble(agentStatus: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .animateContentSize(),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = ConversationSide.Receiver,
            modifier = Modifier.padding(bottom = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = ConversationSide.Receiver,
                modifier = Modifier
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            brush = UiColor.GradientAi,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "A",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = UiColor.AiBubbleForeground
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "Axon",
                    fontSize = 14.sp,
                    color = UiColor.MutedForeground
                )
            }
        }

        Column(
            modifier = Modifier
                .widthIn(max = with(LocalDensity.current) { (ScreenUtils.getScreenWidth() * 0.8f).toDp() })
                .background(
                    brush = UiColor.GradientAi,
                    shape = messageBubbleShape(false)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(UiColor.AiBubbleForeground, CircleShape)
                    )

                    Spacer(modifier = Modifier.width(2.dp))
                }

                Spacer(modifier = Modifier.width(3.dp))

                Text(
                    text = agentStatus,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = UiColor.AiBubbleForeground
                )
            }
        }
    }
}
