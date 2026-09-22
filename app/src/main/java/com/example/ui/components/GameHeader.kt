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
fun GameHeader(
    levelNumber: Int,
    moveCount: Int,
    bestMoves: Int,
    isDarkTheme: Boolean,
    onOpenLevelSelect: () -> Unit,
    onOpenHelp: () -> Unit,
    onToggleTheme: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardBg = if (isDarkTheme) Color(0xFF1E293B) else Color(0xFFF1F5F9)
    val borderColor = if (isDarkTheme) Color(0xFF334155) else Color(0xFFE2E8F0)
    val textPrimary = if (isDarkTheme) Color(0xFFF8FAFC) else Color(0xFF0F172A)
    val textSecondary = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        color = cardBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            // Top Row: App Title & Action Emoji Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "🧪",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Color Sort",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                }

                // Header Action Buttons using Emojis
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    EmojiHeaderButton(
                        emoji = "📋",
                        label = "Levels",
                        testTag = "btn_levels",
                        isDarkTheme = isDarkTheme,
                        onClick = onOpenLevelSelect
                    )
                    EmojiHeaderButton(
                        emoji = "❓",
                        label = "Rules",
                        testTag = "btn_help",
                        isDarkTheme = isDarkTheme,
                        onClick = onOpenHelp
                    )
                    EmojiHeaderButton(
                        emoji = if (isDarkTheme) "☀️" else "🌙",
                        label = if (isDarkTheme) "Light" else "Dark",
                        testTag = "btn_theme",
                        isDarkTheme = isDarkTheme,
                        onClick = onToggleTheme
                    )
                }
            }

            // Stat pills: Level, Current Moves, Best Record
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatBadge(
                    emoji = "🏆",
                    label = "Level",
                    value = "$levelNumber",
                    isDarkTheme = isDarkTheme
                )
                StatBadge(
                    emoji = "🎯",
                    label = "Moves",
                    value = "$moveCount",
                    isDarkTheme = isDarkTheme
                )
                StatBadge(
                    emoji = "⭐️",
                    label = "Best",
                    value = if (bestMoves > 0) "$bestMoves" else "-",
                    isDarkTheme = isDarkTheme
                )
            }
        }
    }
}

@Composable
private fun EmojiHeaderButton(
    emoji: String,
    label: String,
    testTag: String,
    isDarkTheme: Boolean,
    onClick: () -> Unit
) {
    val btnBg = if (isDarkTheme) Color(0xFF334155) else Color(0xFFE2E8F0)
    val textCol = if (isDarkTheme) Color(0xFFE2E8F0) else Color(0xFF334155)

    Box(
        modifier = Modifier
            .testTag(testTag)
            .clip(RoundedCornerShape(10.dp))
            .background(btnBg)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = onClick
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = emoji, fontSize = 14.sp)
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = textCol
            )
        }
    }
}

@Composable
private fun StatBadge(
    emoji: String,
    label: String,
    value: String,
    isDarkTheme: Boolean
) {
    val pillBg = if (isDarkTheme) Color(0xFF0F172A) else Color(0xFFFFFFFF)
    val borderColor = if (isDarkTheme) Color(0xFF334155) else Color(0xFFE2E8F0)
    val textPrimary = if (isDarkTheme) Color(0xFFF1F5F9) else Color(0xFF0F172A)
    val textSecondary = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(pillBg)
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(text = emoji, fontSize = 16.sp)
        Column {
            Text(text = label, fontSize = 10.sp, color = textSecondary)
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary)
        }
    }
}
