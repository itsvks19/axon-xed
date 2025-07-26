package com.axon.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

sealed interface ConversationSide : Arrangement.Horizontal {
    data object Sender : ConversationSide {
        override fun Density.arrange(
            totalSize: Int,
            sizes: IntArray,
            layoutDirection: LayoutDirection,
            outPositions: IntArray
        ) {
            with(Arrangement.Start) {
                arrange(
                    totalSize = totalSize,
                    sizes = sizes,
                    layoutDirection = when (layoutDirection) {
                        LayoutDirection.Ltr -> LayoutDirection.Rtl
                        LayoutDirection.Rtl -> LayoutDirection.Ltr
                    },
                    outPositions = outPositions
                )
            }
        }
    }

    data object Receiver : ConversationSide, Arrangement.Horizontal by Arrangement.Start
}
