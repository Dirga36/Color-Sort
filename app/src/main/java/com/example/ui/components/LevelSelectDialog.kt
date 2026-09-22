package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.logic.GameEngine

@Composable
fun LevelSelectDialog(
    currentLevel: Int,
    maxUnlockedLevel: Int,
    isDarkTheme: Boolean,
    onSelectLevel: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val dialogBg = if (isDarkTheme) Color(0xFF1E293B) else Color(0xFFFFFFFF)
    val borderColor = if (isDarkTheme) Color(0xFF334155) else Color(0xFFE2E8F0)
    val textPrimary = if (isDarkTheme) Color(0xFFF8FAFC) else Color(0xFF0F172A)
    val textSecondary = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)

    // Total display levels (from 1 to at least 15 or maxUnlocked + 2)
    val totalLevels = maxOf(15, maxUnlockedLevel + 1)
    val levelList = (1..totalLevels).toList()

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = dialogBg,
            border = androidx.compose.foundation.BorderStroke(2.dp, borderColor),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("level_select_dialog")
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📋 Select Level",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                    Text(
                        text = "Unlocked: $maxUnlockedLevel",
                        fontSize = 12.sp,
                        color = textSecondary
                    )
                }

                Text(
                    text = "Tap any unlocked level to play:",
                    fontSize = 13.sp,
                    color = textSecondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 16.dp)
                )

                // Level items grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    contentPadding = PaddingValues(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 340.dp)
                ) {
                    items(levelList) { levelNum ->
                        val isUnlocked = levelNum <= maxUnlockedLevel
                        val isCurrent = levelNum == currentLevel

                        val itemBg = when {
                            isCurrent -> if (isDarkTheme) Color(0xFF3B82F6) else Color(0xFF2563EB)
                            isUnlocked -> if (isDarkTheme) Color(0xFF334155) else Color(0xFFE2E8F0)
                            else -> if (isDarkTheme) Color(0x33334155) else Color(0x33CBD5E1)
                        }

                        val itemBorder = when {
                            isCurrent -> Color(0xFFF59E0B)
                            isUnlocked -> if (isDarkTheme) Color(0xFF475569) else Color(0xFFCBD5E1)
                            else -> Color.Transparent
                        }

                        val itemTextCol = when {
                            isCurrent -> Color.White
                            isUnlocked -> if (isDarkTheme) Color(0xFFF8FAFC) else Color(0xFF0F172A)
                            else -> if (isDarkTheme) Color(0xFF64748B) else Color(0xFF94A3B8)
                        }

                        Box(
                            modifier = Modifier
                                .testTag("level_btn_$levelNum")
                                .size(64.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(itemBg)
                                .border(
                                    width = if (isCurrent) 2.dp else 1.dp,
                                    color = itemBorder,
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .clickable(enabled = isUnlocked) {
                                    onSelectLevel(levelNum)
                                    onDismiss()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                if (isUnlocked) {
                                    Text(
                                        text = "$levelNum",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = itemTextCol
                                    )
                                    if (levelNum < maxUnlockedLevel) {
                                        Text(text = "⭐️", fontSize = 10.sp)
                                    } else if (isCurrent) {
                                        Text(text = "▶️", fontSize = 10.sp)
                                    }
                                } else {
                                    Text(text = "🔒", fontSize = 18.sp)
                                    Text(
                                        text = "$levelNum",
                                        fontSize = 11.sp,
                                        color = itemTextCol
                                    )
                                }
                            }
                        }
                    }
                }

                // Close Button
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Close ✖️", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
