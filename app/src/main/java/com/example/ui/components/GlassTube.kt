package com.example.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ItemColor
import com.example.model.Tube

@Composable
fun GlassTube(
    tube: Tube,
    isSelected: Boolean,
    isHintSource: Boolean,
    isHintTarget: Boolean,
    isDarkTheme: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    // Flat colors strictly without gradients
    val glassBg = if (isDarkTheme) Color(0x1F2A374A) else Color(0x120F172A)
    val glassBorderColor = when {
        isSelected -> Color(0xFFF59E0B) // Amber gold border for selection
        isHintSource -> Color(0xFF3B82F6) // Blue for hint source
        isHintTarget -> Color(0xFF10B981) // Green for hint target
        tube.isComplete -> Color(0xFF10B981) // Emerald border for complete
        isDarkTheme -> Color(0x4094A3B8)
        else -> Color(0x3064748B)
    }

    val borderWidth = if (isSelected || isHintSource || isHintTarget) 3.dp else 2.dp
    val tubeShape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp, topStart = 6.dp, topEnd = 6.dp)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .testTag("tube_${tube.id}")
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = false, radius = 40.dp),
                onClick = onClick
            )
            .padding(horizontal = 4.dp, vertical = 6.dp)
    ) {
        // Status / Hint / Complete badge above the tube
        Box(
            modifier = Modifier
                .height(24.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            when {
                tube.isComplete -> {
                    Text(text = "✨ Done", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                }
                isHintSource -> {
                    Text(text = "FROM 👆", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3B82F6))
                }
                isHintTarget -> {
                    Text(text = "TO 👇", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                }
                isSelected -> {
                    Text(text = "Selected 🧪", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color(0xFFF59E0B))
                }
            }
        }

        // The Glass Tube Container
        Box(
            modifier = Modifier
                .width(58.dp)
                .height(180.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            // Glass Rim Lip at top
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .width(64.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(glassBorderColor)
            )

            // Glass Body
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 4.dp)
                    .clip(tubeShape)
                    .background(glassBg)
                    .border(width = borderWidth, color = glassBorderColor, shape = tubeShape)
            ) {
                // Measurement tick marks (flat white/slate dashes)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 4.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .width(6.dp)
                                .height(1.5.dp)
                                .background(if (isDarkTheme) Color(0x33FFFFFF) else Color(0x22000000))
                        )
                    }
                }

                // Color Segments inside the tube (stacked from bottom to top)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 6.dp, vertical = 6.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val items = tube.items
                    val topItemIndex = items.size - 1

                    for (i in 0 until tube.capacity) {
                        val itemIndex = tube.capacity - 1 - i
                        val item = items.getOrNull(itemIndex)

                        if (item != null) {
                            val isTopItem = itemIndex == topItemIndex
                            val shouldElevate = isTopItem && isSelected

                            // Animate elevation when selected
                            val verticalOffset by animateDpAsState(
                                targetValue = if (shouldElevate) (-24).dp else 0.dp,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMedium
                                ),
                                label = "item_elevation"
                            )

                            ColorSegmentItem(
                                item = item,
                                modifier = Modifier
                                    .offset(y = verticalOffset)
                                    .padding(vertical = 2.dp)
                            )
                        } else {
                            // Empty slot placeholder space
                            Spacer(modifier = Modifier.height(38.dp))
                        }
                    }
                }
            }
        }

        // Tube ID label
        Text(
            text = "#${tube.id + 1}",
            style = MaterialTheme.typography.labelSmall,
            color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun ColorSegmentItem(
    item: ItemColor,
    modifier: Modifier = Modifier
) {
    // Solid flat pill shape for each liquid segment (no gradients!)
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(34.dp),
        shape = RoundedCornerShape(10.dp),
        color = item.color,
        shadowElevation = 0.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Flat highlight line near top edge for clean glass/surface reflection (no gradient)
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 3.dp)
                    .width(28.dp)
                    .height(2.dp)
                    .clip(RoundedCornerShape(1.dp))
                    .background(Color(0x40FFFFFF))
            )

            // Centered Emoji symbol representing the item color
            Text(
                text = item.emoji,
                fontSize = 15.sp,
                lineHeight = 15.sp
            )
        }
    }
}
