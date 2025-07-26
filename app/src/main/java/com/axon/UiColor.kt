package com.axon

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object UiColor {
    val Background = Color(0xFF111317)
    val Foreground = Color(0xFFe8e8ee)

    val Card = Color(0xFF16181d)
    val CardForeground = Color(0xFFe8e8ee)

    val Popover = Color(0xFF16181d)
    val PopoverForeground = Color(0xFFe8e8ee)

    val Primary = Color(0xFF6666ff)
    val PrimaryForeground = Color(0xFF111317)
    val PrimaryGlow = Color(0xFF9999ff)

    val Secondary = Color(0xFF21242c)
    val SecondaryForeground = Color(0xFFe8e8ee)

    val Muted = Color(0xFF21242c)
    val MutedForeground = Color(0xFFababba)

    val Accent = Color(0xFFaa80ff)
    val AccentForeground = Color(0xFF111317)

    val Destructive = Color(0xFFf25a5a)
    val DestructiveForeground = Color(0xFF111317)

    val Success = Color(0xFF2bee6c)
    val SuccessForeground = Color(0xFF111317)

    val Border = Color(0xFF2b303b)
    val Input = Color(0xFF272c35)
    val Ring = Color(0xFF6666ff)

    val AiBubble = Color(0xFF1a1d23)
    val AiBubbleForeground = Color(0xFFe8e8ee)
    val UserBubble = Color(0xFF6666ff)
    val UserBubbleForeground = Color(0xFF111317)

    val GradientPrimary = Brush.linearGradient(listOf(Primary, PrimaryGlow))
    val GradientAi = Brush.linearGradient(listOf(AiBubble, Color(0xFF21242c)))
    val GradientUser = Brush.linearGradient(listOf(UserBubble, Color(0xFF9d8aff)))
}
