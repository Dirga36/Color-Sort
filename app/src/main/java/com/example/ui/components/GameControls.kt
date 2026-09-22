package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameControls(
    canUndo: Boolean,
    extraTubeUsed: Boolean,
    isDarkTheme: Boolean,
    onUndo: () -> Unit,
    onRestart: () -> Unit,
    onHint: () -> Unit,
    onAddExtraTube: () -> Unit,
    modifier: Modifier = Modifier
) {
    val barBg = if (isDarkTheme) Color(0xFF1E293B) else Color(0xFFF1F5F9)
    val borderColor = if (isDarkTheme) Color(0xFF334155) else Color(0xFFCBD5E1)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        color = barBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Undo Button with ↩️ emoji
            ActionButton(
                emoji = "↩️",
                label = "Undo",
                enabled = canUndo,
                testTag = "btn_undo",
                isDarkTheme = isDarkTheme,
                onClick = onUndo
            )

            // Restart Button with 🔄 emoji
            ActionButton(
                emoji = "🔄",
                label = "Restart",
                enabled = true,
                testTag = "btn_restart",
                isDarkTheme = isDarkTheme,
                onClick = onRestart
            )

            // Hint Button with 💡 emoji
            ActionButton(
                emoji = "💡",
                label = "Hint",
                enabled = true,
                testTag = "btn_hint",
                isDarkTheme = isDarkTheme,
                onClick = onHint
            )

            // Extra Tube Button with 🧪➕ emoji
            ActionButton(
                emoji = if (extraTubeUsed) "🧪✔️" else "🧪➕",
                label = if (extraTubeUsed) "Added" else "+Tube",
                enabled = !extraTubeUsed,
                testTag = "btn_extra_tube",
                isDarkTheme = isDarkTheme,
                onClick = onAddExtraTube
            )
        }
    }
}

@Composable
private fun ActionButton(
    emoji: String,
    label: String,
    enabled: Boolean,
    testTag: String,
    isDarkTheme: Boolean,
    onClick: () -> Unit
) {
    val baseBg = when {
        !enabled -> if (isDarkTheme) Color(0x33334155) else Color(0x33CBD5E1)
        isDarkTheme -> Color(0xFF334155)
        else -> Color(0xFFE2E8F0)
    }

    val textCol = when {
        !enabled -> if (isDarkTheme) Color(0xFF64748B) else Color(0xFF94A3B8)
        isDarkTheme -> Color(0xFFF8FAFC)
        else -> Color(0xFF0F172A)
    }

    val borderColor = if (enabled) {
        if (isDarkTheme) Color(0xFF475569) else Color(0xFFCBD5E1)
    } else {
        Color.Transparent
    }

    Box(
        modifier = Modifier
            .testTag(testTag)
            .size(width = 72.dp, height = 58.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(baseBg)
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable(
                enabled = enabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = emoji,
                fontSize = 20.sp,
                lineHeight = 22.sp
            )
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = textCol,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}
