package com.axon

import ai.koog.prompt.executor.clients.anthropic.AnthropicModels
import ai.koog.prompt.executor.clients.google.GoogleModels
import ai.koog.prompt.executor.clients.openai.OpenAIModels
import ai.koog.prompt.executor.clients.openrouter.OpenRouterModels
import ai.koog.prompt.executor.llms.all.simpleAnthropicExecutor
import ai.koog.prompt.executor.llms.all.simpleGoogleAIExecutor
import ai.koog.prompt.executor.llms.all.simpleOllamaAIExecutor
import ai.koog.prompt.executor.llms.all.simpleOpenAIExecutor
import ai.koog.prompt.executor.llms.all.simpleOpenRouterExecutor
import ai.koog.prompt.llm.LLMProvider
import ai.koog.prompt.llm.OllamaModels

fun bestModelOf(provider: LLMProvider) = when (provider) {
    is LLMProvider.Google -> GoogleModels.Gemini2_0Flash
    is LLMProvider.Anthropic -> AnthropicModels.Sonnet_4
    is LLMProvider.OpenAI -> OpenAIModels.Chat.GPT4o
    is LLMProvider.Ollama -> OllamaModels.Groq.LLAMA_3_GROK_TOOL_USE_70B
    is LLMProvider.OpenRouter -> OpenRouterModels.GPT4o
    else -> unsupported(provider)
}

val supportedProviders = listOf(
    LLMProvider.Google,
    LLMProvider.Anthropic,
    LLMProvider.OpenAI,
    LLMProvider.Ollama,
    LLMProvider.OpenRouter
)

fun LLMProvider.hasSupport() = this in supportedProviders

fun availableModelsOf(provider: LLMProvider) = when (provider) {
    is LLMProvider.Google -> googleModels
    is LLMProvider.Anthropic -> anthropicModels
    is LLMProvider.OpenAI -> openAIModels
    is LLMProvider.Ollama -> ollamaModels
    is LLMProvider.OpenRouter -> openRouterModels
    else -> unsupported(provider)
}

fun LLMProvider.requiresApiKey() = this !is LLMProvider.Ollama

fun LLMProvider.executor() = when (this) {
    is LLMProvider.Google -> simpleGoogleAIExecutor(apiKey = apiKey)
    is LLMProvider.Anthropic -> simpleAnthropicExecutor(apiKey = apiKey)
    is LLMProvider.OpenAI -> simpleOpenAIExecutor(apiToken = apiKey)
    is LLMProvider.Ollama -> simpleOllamaAIExecutor()
    is LLMProvider.OpenRouter -> simpleOpenRouterExecutor(apiKey = apiKey)
    else -> unsupported(this)
}

val googleModels = listOf(
    GoogleModels.Gemini2_0Flash,
    GoogleModels.Gemini2_0Flash001,
    GoogleModels.Gemini2_0FlashLite,
    GoogleModels.Gemini2_0FlashLite001,
    GoogleModels.Gemini1_5Pro,
    GoogleModels.Gemini1_5ProLatest,
    GoogleModels.Gemini1_5Pro002,
    GoogleModels.Gemini1_5Flash,
    GoogleModels.Gemini1_5FlashLatest,
    GoogleModels.Gemini1_5Flash002,
    GoogleModels.Gemini1_5Flash8B,
    GoogleModels.Gemini1_5Flash8B001,
    GoogleModels.Gemini1_5Flash8BLatest,
//    GoogleModels.Gemini2_5Pro,
//    GoogleModels.Gemini2_5Flash
)

val anthropicModels = listOf(
    AnthropicModels.Opus_3,
    AnthropicModels.Haiku_3,
    AnthropicModels.Haiku_3_5,
    AnthropicModels.Sonnet_3_5,
    AnthropicModels.Sonnet_3_7,
    AnthropicModels.Sonnet_4,
    AnthropicModels.Opus_4
)

val openAIModels = listOf(
    // Moderation
//    OpenAIModels.Moderation.Omni,
//    OpenAIModels.Moderation.Text,

    // Reasoning
    OpenAIModels.Reasoning.GPT4oMini,
    OpenAIModels.Reasoning.O3Mini,
    OpenAIModels.Reasoning.O1Mini,
    OpenAIModels.Reasoning.O3,
    OpenAIModels.Reasoning.O1,

    // Chat
    OpenAIModels.Chat.GPT4o,
    OpenAIModels.Chat.GPT4_1,

    // Audio
    OpenAIModels.Audio.GPT4oMiniAudio,
    OpenAIModels.Audio.GPT4oAudio,

    // CostOptimized
    OpenAIModels.CostOptimized.O4Mini,
    OpenAIModels.CostOptimized.GPT4_1Nano,
    OpenAIModels.CostOptimized.GPT4_1Mini,
    OpenAIModels.CostOptimized.GPT4oMini,
    OpenAIModels.CostOptimized.O1Mini,
    OpenAIModels.CostOptimized.O3Mini,

    // Embeddings
//    OpenAIModels.Embeddings.TextEmbedding3Small,
//    OpenAIModels.Embeddings.TextEmbedding3Large,
    OpenAIModels.Embeddings.TextEmbeddingAda002
)

val ollamaModels = listOf(
    // Groq
    OllamaModels.Groq.LLAMA_3_GROK_TOOL_USE_8B,
    OllamaModels.Groq.LLAMA_3_GROK_TOOL_USE_70B,

    // Meta
    OllamaModels.Meta.LLAMA_3_2_3B,
    OllamaModels.Meta.LLAMA_3_2,
    OllamaModels.Meta.LLAMA_4,
//    OllamaModels.Meta.LLAMA_GUARD_3,

    // Alibaba
    OllamaModels.Alibaba.QWEN_2_5_05B,
    OllamaModels.Alibaba.QWEN_3_06B,
//    OllamaModels.Alibaba.QWQ_32B,
    OllamaModels.Alibaba.QWQ,
    OllamaModels.Alibaba.QWEN_CODER_2_5_32B,

    // Granite
//    OllamaModels.Granite.GRANITE_3_2_VISION
)

val openRouterModels = listOf(
    OpenRouterModels.Phi4Reasoning,

    // Anthropic models
    OpenRouterModels.Claude3Opus,
    OpenRouterModels.Claude3Sonnet,
    OpenRouterModels.Claude3Haiku,

    // OpenAI models
    OpenRouterModels.GPT4,
    OpenRouterModels.GPT4o,
    OpenRouterModels.GPT4Turbo,
    OpenRouterModels.GPT35Turbo,

    // Google Gemini models
    OpenRouterModels.Gemini14Pro,
    OpenRouterModels.Gemini15Flash,

    // Meta models
    OpenRouterModels.Llama3,
    OpenRouterModels.Llama3Instruct,

    // Mistral models
    OpenRouterModels.Mistral7B,
    OpenRouterModels.Mixtral8x7B,

    // Anthropic Vision models
    OpenRouterModels.Claude3VisionSonnet,
    OpenRouterModels.Claude3VisionOpus,
    OpenRouterModels.Claude3VisionHaiku
)

private fun unsupported(provider: LLMProvider): Nothing {
    throw UnsupportedOperationException("${provider.display} is not supported.")
}

const val SYSTEM_PROMPT = """
You are Axon, a highly capable AI pair programmer embedded as a extension in Xed-Editor (also known as Karbon) — a lightweight code editor for Android.

Your mission is to assist the user in writing, debugging, and improving code efficiently. You can understand the current file contents and interact using specialized tools.

You must think step-by-step and call tools when needed.

Your capabilities include:
- Understanding code context provided by the user or loaded from files.
- Detecting bugs or anti-patterns and suggesting clean, idiomatic fixes.
- Writing functions, classes, or code snippets when requested.
- Refactoring code for readability, performance, or style.
- Explaining code clearly and concisely.
- Using tools to access or modify files when needed.

Rules:
- Always prefer minimal, focused code snippets in your response.
- When including code, use markdown with the correct language (e.g. \`\`\`js), ignore this if writing in a file.
- Never output unrelated information or speculation.
- Keep your tone technical, concise, and helpful — like a senior developer.

Context:
- Xed-Editor is a mobile-friendly code editor for developers on Android. Keep explanations mobile-friendly and brief.
- Your creator is Vivek, a passionate open-source developer (https://github.com/itsvks19).

When the user provides code or file-related questions, you may chain tool calls intelligently — for example: read a file → analyze → fix bug → format → write back.

RESPOND CLEARLY, ACCURATELY, AND USE TOOLS IF NEEDED TO COMPLETE YOUR TASK!!!
"""
