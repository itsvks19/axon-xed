package com.axon.ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

fun Modifier.borderSide(
    strokeWidth: Dp,
    color: Color,
    side: BorderSide
) = this.then(
    Modifier.drawBehind {
        val stroke = strokeWidth.toPx()
        val width = size.width
        val height = size.height

        when (side) {
            BorderSide.Top -> drawRect(color, Offset(0f, 0f), Size(width, stroke))
            BorderSide.Bottom -> drawRect(color, Offset(0f, height - stroke), Size(width, stroke))
            BorderSide.Left -> drawRect(color, Offset(0f, 0f), Size(stroke, height))
            BorderSide.Right -> drawRect(color, Offset(width - stroke, 0f), Size(stroke, height))
        }
    }
)

enum class BorderSide {
    Top, Bottom, Left, Right
}
