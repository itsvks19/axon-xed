package com.axon

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.agent.config.AIAgentConfig
import ai.koog.agents.core.dsl.builder.forwardTo
import ai.koog.agents.core.dsl.builder.strategy
import ai.koog.agents.core.dsl.extension.nodeExecuteTool
import ai.koog.agents.core.dsl.extension.nodeLLMRequest
import ai.koog.agents.core.dsl.extension.nodeLLMSendToolResult
import ai.koog.agents.core.dsl.extension.onAssistantMessage
import ai.koog.agents.core.dsl.extension.onToolCall
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import ai.koog.agents.core.tools.reflect.asTools
import ai.koog.agents.features.eventHandler.feature.EventHandler
import ai.koog.prompt.llm.LLMProvider

private val agentStrategy = strategy("Axon") {
    val nodeSendInput by nodeLLMRequest()
    val nodeExecuteTool by nodeExecuteTool()
    val nodeSendToolResult by nodeLLMSendToolResult()

    // Start -> Send input
    edge(nodeStart forwardTo nodeSendInput)

    // Send input -> Finish
    edge(
        (nodeSendInput forwardTo nodeFinish)
                transformed { it }
                onAssistantMessage { true }
    )

    // Send input -> Execute tool
    edge(
        (nodeSendInput forwardTo nodeExecuteTool)
                onToolCall { true }
    )

    // Execute tool -> Send the tool result
    edge(nodeExecuteTool forwardTo nodeSendToolResult)

    // Send the tool result -> finish
    edge(
        (nodeSendToolResult forwardTo nodeFinish)
                transformed { it }
                onAssistantMessage { true }
    )
}

// a simple calculator tool that can add two numbers
@LLMDescription("Tools for performing basic arithmetic operations")
class CalculatorTools : ToolSet {
    @Tool
    @LLMDescription("Add two numbers together and return their sum")
    fun add(
        @LLMDescription("First number to add (integer value)")
        num1: Int,

        @LLMDescription("Second number to add (integer value)")
        num2: Int
    ): String {
        val sum = num1 + num2
        return "The sum of $num1 and $num2 is: $sum"
    }
}

private val toolRegistry = ToolRegistry {
    tools(CalculatorTools().asTools())
}

fun createAgent(provider: LLMProvider, config: AIAgentConfig) = AIAgent(
    promptExecutor = provider.executor(),
    toolRegistry = toolRegistry,
    strategy = agentStrategy,
    agentConfig = config,
    installFeatures = {
        install(EventHandler) {
//            onBeforeAgentStarted { eventContext: AgentStartContext<*> ->
//                println("Starting strategy: ${eventContext.strategy.name}")
//            }
//            onAgentFinished { eventContext: AgentFinishedContext ->
//                println("Result: ${eventContext.result}")
//            }
            onBeforeAgentStarted { strategy, agent ->
                println("Starting strategy: ${strategy.name}")
            }

            onAgentFinished { strategyName, result ->
                println("Result: $result")
            }

            onToolCall { tool, toolArgs ->
                println("Tool call ${tool.name} with args $toolArgs")
            }

            onToolCallResult { tool, toolArgs, result ->
                println("${tool.name} result is ${result?.toStringDefault()}")
            }
        }
    }
)
