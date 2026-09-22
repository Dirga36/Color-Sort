package com.example.model

import androidx.compose.ui.graphics.Color

/**
 * Vibrant flat colors for the puzzle game (strictly without gradients).
 * Each color has a unique hue, display name, and matching emoji icon.
 */
enum class ItemColor(
    val displayName: String,
    val color: Color,
    val onColor: Color,
    val emoji: String
) {
    RED("Ruby", Color(0xFFEF4444), Color.White, "🔴"),
    BLUE("Cobalt", Color(0xFF3B82F6), Color.White, "🔵"),
    GREEN("Emerald", Color(0xFF10B981), Color.White, "🟢"),
    YELLOW("Amber", Color(0xFFF59E0B), Color.Black, "🟡"),
    PURPLE("Amethyst", Color(0xFF8B5CF6), Color.White, "🟣"),
    ORANGE("Tangerine", Color(0xFFF97316), Color.White, "🟠"),
    CYAN("Aqua", Color(0xFF06B6D4), Color.Black, "🔷"),
    PINK("Rose", Color(0xFFEC4899), Color.White, "🌸"),
    LIME("Lime", Color(0xFF84CC16), Color.Black, "🍏"),
    INDIGO("Indigo", Color(0xFF6366F1), Color.White, "🫐");
}

/**
 * Representation of a single glass container (tube / vial).
 * items list ordered from bottom (index 0) to top (last index).
 */
data class Tube(
    val id: Int,
    val items: List<ItemColor> = emptyList(),
    val capacity: Int = 4
) {
    val isFull: Boolean get() = items.size >= capacity
    val isEmpty: Boolean get() = items.isEmpty()
    val isComplete: Boolean get() = isFull && items.all { it == items.first() }
    val topColor: ItemColor? get() = items.lastOrNull()
    val availableSpace: Int get() = capacity - items.size

    fun canAccept(color: ItemColor): Boolean {
        if (isFull) return false
        if (isEmpty) return true
        return topColor == color
    }
}

/**
 * Record of a move for undo functionality.
 */
data class MoveRecord(
    val fromTubeIndex: Int,
    val toTubeIndex: Int,
    val color: ItemColor
)

/**
 * Game level definition.
 */
data class GameLevel(
    val levelNumber: Int,
    val title: String,
    val tubes: List<List<ItemColor>>,
    val emptyTubesCount: Int = 2,
    val parMoves: Int = 10
)

/**
 * Game progression state.
 */
enum class GameStatus {
    PLAYING,
    WON
}
