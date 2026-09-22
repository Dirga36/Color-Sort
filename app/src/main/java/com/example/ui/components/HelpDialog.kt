package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun HelpDialog(
    isDarkTheme: Boolean,
    onDismiss: () -> Unit
) {
    val dialogBg = if (isDarkTheme) Color(0xFF1E293B) else Color(0xFFFFFFFF)
    val borderColor = if (isDarkTheme) Color(0xFF334155) else Color(0xFFE2E8F0)
    val textPrimary = if (isDarkTheme) Color(0xFFF8FAFC) else Color(0xFF0F172A)
    val textSecondary = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = dialogBg,
            border = androidx.compose.foundation.BorderStroke(2.dp, borderColor),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("help_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "🧪 How to Play",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )

                Text(
                    text = "Sort all colors across the glass containers until each container is neatly uniform!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = textSecondary
                )

                RuleItem(
                    emoji = "1️⃣",
                    title = "Pick Up",
                    desc = "Tap any glass container to select its topmost colored item.",
                    isDarkTheme = isDarkTheme
                )

                RuleItem(
                    emoji = "2️⃣",
                    title = "Pour / Transfer",
                    desc = "Tap another container to move the item there. Items move one at a time.",
                    isDarkTheme = isDarkTheme
                )

                RuleItem(
                    emoji = "3️⃣",
                    title = "Sorting Rule",
                    desc = "You can only place an item on top of the same matching color, or into an empty container.",
                    isDarkTheme = isDarkTheme
                )

                RuleItem(
                    emoji = "4️⃣",
                    title = "Container Capacity",
                    desc = "Each container holds up to 4 items maximum.",
                    isDarkTheme = isDarkTheme
                )

                RuleItem(
                    emoji = "💡",
                    title = "Tools & Assistance",
                    desc = "Use ↩️ Undo to step back, 💡 Hint to see a smart move, or 🧪➕ to add a buffer container!",
                    isDarkTheme = isDarkTheme
                )

                ElevatedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Color(0xFF3B82F6),
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Got It! 👍", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun RuleItem(
    emoji: String,
    title: String,
    desc: String,
    isDarkTheme: Boolean
) {
    val textPrimary = if (isDarkTheme) Color(0xFFF8FAFC) else Color(0xFF0F172A)
    val textSecondary = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(text = emoji, fontSize = 20.sp)
        Column {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = textPrimary
            )
            Text(
                text = desc,
                fontSize = 13.sp,
                color = textSecondary
            )
        }
    }
}
