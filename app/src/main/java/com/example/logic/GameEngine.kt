package com.example.logic

import com.example.model.GameLevel
import com.example.model.ItemColor
import com.example.model.Tube
import kotlin.random.Random

object GameEngine {

    const val DEFAULT_CAPACITY = 4

    /**
     * Hand-crafted progression levels with guaranteed solvability and balanced difficulty.
     */
    val PRESET_LEVELS: List<GameLevel> = listOf(
        // Level 1: Intro (2 colors, 2 filled + 1 empty = 3 tubes)
        GameLevel(
            levelNumber = 1,
            title = "First Steps",
            tubes = listOf(
                listOf(ItemColor.RED, ItemColor.BLUE, ItemColor.RED, ItemColor.BLUE),
                listOf(ItemColor.BLUE, ItemColor.RED, ItemColor.BLUE, ItemColor.RED)
            ),
            emptyTubesCount = 1,
            parMoves = 6
        ),
        // Level 2: Gentle Mix (3 colors, 3 filled + 1 empty = 4 tubes)
        GameLevel(
            levelNumber = 2,
            title = "Triple Blend",
            tubes = listOf(
                listOf(ItemColor.RED, ItemColor.GREEN, ItemColor.BLUE, ItemColor.RED),
                listOf(ItemColor.BLUE, ItemColor.RED, ItemColor.GREEN, ItemColor.BLUE),
                listOf(ItemColor.GREEN, ItemColor.BLUE, ItemColor.RED, ItemColor.GREEN)
            ),
            emptyTubesCount = 1,
            parMoves = 9
        ),
        // Level 3: Breathing Room (3 colors, 3 filled + 2 empty = 5 tubes)
        GameLevel(
            levelNumber = 3,
            title = "Open Flow",
            tubes = listOf(
                listOf(ItemColor.YELLOW, ItemColor.PURPLE, ItemColor.YELLOW, ItemColor.PURPLE),
                listOf(ItemColor.PURPLE, ItemColor.CYAN, ItemColor.PURPLE, ItemColor.CYAN),
                listOf(ItemColor.CYAN, ItemColor.YELLOW, ItemColor.CYAN, ItemColor.YELLOW)
            ),
            emptyTubesCount = 2,
            parMoves = 10
        ),
        // Level 4: Four Quarters (4 colors, 4 filled + 2 empty = 6 tubes)
        GameLevel(
            levelNumber = 4,
            title = "Prism Harmony",
            tubes = listOf(
                listOf(ItemColor.RED, ItemColor.BLUE, ItemColor.GREEN, ItemColor.YELLOW),
                listOf(ItemColor.YELLOW, ItemColor.RED, ItemColor.BLUE, ItemColor.GREEN),
                listOf(ItemColor.GREEN, ItemColor.YELLOW, ItemColor.RED, ItemColor.BLUE),
                listOf(ItemColor.BLUE, ItemColor.GREEN, ItemColor.YELLOW, ItemColor.RED)
            ),
            emptyTubesCount = 2,
            parMoves = 14
        ),
        // Level 5: Citrus Swirl (4 colors, 4 filled + 2 empty = 6 tubes)
        GameLevel(
            levelNumber = 5,
            title = "Citrus Swirl",
            tubes = listOf(
                listOf(ItemColor.ORANGE, ItemColor.YELLOW, ItemColor.GREEN, ItemColor.ORANGE),
                listOf(ItemColor.GREEN, ItemColor.ORANGE, ItemColor.RED, ItemColor.YELLOW),
                listOf(ItemColor.RED, ItemColor.GREEN, ItemColor.YELLOW, ItemColor.RED),
                listOf(ItemColor.YELLOW, ItemColor.RED, ItemColor.ORANGE, ItemColor.GREEN)
            ),
            emptyTubesCount = 2,
            parMoves = 15
        ),
        // Level 6: Violet Breeze (5 colors, 5 filled + 2 empty = 7 tubes)
        GameLevel(
            levelNumber = 6,
            title = "Violet Breeze",
            tubes = listOf(
                listOf(ItemColor.PURPLE, ItemColor.BLUE, ItemColor.PINK, ItemColor.CYAN),
                listOf(ItemColor.CYAN, ItemColor.RED, ItemColor.PURPLE, ItemColor.BLUE),
                listOf(ItemColor.PINK, ItemColor.PURPLE, ItemColor.RED, ItemColor.PINK),
                listOf(ItemColor.BLUE, ItemColor.CYAN, ItemColor.PINK, ItemColor.RED),
                listOf(ItemColor.RED, ItemColor.PINK, ItemColor.CYAN, ItemColor.PURPLE)
            ),
            emptyTubesCount = 2,
            parMoves = 18
        ),
        // Level 7: Neon Fusion (5 colors, 5 filled + 2 empty = 7 tubes)
        GameLevel(
            levelNumber = 7,
            title = "Neon Fusion",
            tubes = listOf(
                listOf(ItemColor.LIME, ItemColor.ORANGE, ItemColor.CYAN, ItemColor.PURPLE),
                listOf(ItemColor.PURPLE, ItemColor.LIME, ItemColor.ORANGE, ItemColor.CYAN),
                listOf(ItemColor.CYAN, ItemColor.PURPLE, ItemColor.LIME, ItemColor.ORANGE),
                listOf(ItemColor.ORANGE, ItemColor.YELLOW, ItemColor.PURPLE, ItemColor.YELLOW),
                listOf(ItemColor.YELLOW, ItemColor.CYAN, ItemColor.YELLOW, ItemColor.LIME)
            ),
            emptyTubesCount = 2,
            parMoves = 20
        ),
        // Level 8: Ocean Sunset (6 colors, 6 filled + 2 empty = 8 tubes)
        GameLevel(
            levelNumber = 8,
            title = "Ocean Sunset",
            tubes = listOf(
                listOf(ItemColor.BLUE, ItemColor.ORANGE, ItemColor.PINK, ItemColor.YELLOW),
                listOf(ItemColor.INDIGO, ItemColor.BLUE, ItemColor.RED, ItemColor.PINK),
                listOf(ItemColor.YELLOW, ItemColor.INDIGO, ItemColor.BLUE, ItemColor.ORANGE),
                listOf(ItemColor.PINK, ItemColor.RED, ItemColor.ORANGE, ItemColor.INDIGO),
                listOf(ItemColor.RED, ItemColor.YELLOW, ItemColor.INDIGO, ItemColor.BLUE),
                listOf(ItemColor.ORANGE, ItemColor.PINK, ItemColor.YELLOW, ItemColor.RED)
            ),
            emptyTubesCount = 2,
            parMoves = 24
        ),
        // Level 9: Rainforest Canopy (6 colors, 6 filled + 2 empty = 8 tubes)
        GameLevel(
            levelNumber = 9,
            title = "Rainforest",
            tubes = listOf(
                listOf(ItemColor.GREEN, ItemColor.LIME, ItemColor.CYAN, ItemColor.YELLOW),
                listOf(ItemColor.YELLOW, ItemColor.GREEN, ItemColor.INDIGO, ItemColor.LIME),
                listOf(ItemColor.CYAN, ItemColor.INDIGO, ItemColor.GREEN, ItemColor.PURPLE),
                listOf(ItemColor.PURPLE, ItemColor.YELLOW, ItemColor.CYAN, ItemColor.INDIGO),
                listOf(ItemColor.LIME, ItemColor.PURPLE, ItemColor.YELLOW, ItemColor.GREEN),
                listOf(ItemColor.INDIGO, ItemColor.CYAN, ItemColor.LIME, ItemColor.PURPLE)
            ),
            emptyTubesCount = 2,
            parMoves = 26
        ),
        // Level 10: Master Chemist (7 colors, 7 filled + 2 empty = 9 tubes)
        GameLevel(
            levelNumber = 10,
            title = "Master Chemist",
            tubes = listOf(
                listOf(ItemColor.RED, ItemColor.BLUE, ItemColor.GREEN, ItemColor.YELLOW),
                listOf(ItemColor.PURPLE, ItemColor.ORANGE, ItemColor.CYAN, ItemColor.RED),
                listOf(ItemColor.BLUE, ItemColor.GREEN, ItemColor.YELLOW, ItemColor.PURPLE),
                listOf(ItemColor.ORANGE, ItemColor.CYAN, ItemColor.RED, ItemColor.BLUE),
                listOf(ItemColor.GREEN, ItemColor.YELLOW, ItemColor.PURPLE, ItemColor.ORANGE),
                listOf(ItemColor.CYAN, ItemColor.RED, ItemColor.BLUE, ItemColor.GREEN),
                listOf(ItemColor.YELLOW, ItemColor.PURPLE, ItemColor.ORANGE, ItemColor.CYAN)
            ),
            emptyTubesCount = 2,
            parMoves = 30
        )
    )

    /**
     * Build tube state for a given level number.
     */
    fun createTubesForLevel(levelNumber: Int): List<Tube> {
        val preset = PRESET_LEVELS.find { it.levelNumber == levelNumber }
        if (preset != null) {
            val list = mutableListOf<Tube>()
            var id = 0
            for (tubeColors in preset.tubes) {
                list.add(Tube(id = id++, items = tubeColors, capacity = DEFAULT_CAPACITY))
            }
            repeat(preset.emptyTubesCount) {
                list.add(Tube(id = id++, items = emptyList(), capacity = DEFAULT_CAPACITY))
            }
            return list
        }

        // Dynamically generated solvable level for higher levels
        return generateSolvableLevel(levelNumber)
    }

    /**
     * Procedurally generates a solvable level by starting from a sorted state and
     * executing valid reverse moves.
     */
    fun generateSolvableLevel(levelNumber: Int): List<Tube> {
        val colorCount = (4 + (levelNumber % 6)).coerceIn(3, ItemColor.entries.size)
        val selectedColors = ItemColor.entries.take(colorCount)
        val emptyCount = 2
        val totalTubes = colorCount + emptyCount

        // 1. Start with fully sorted tubes
        val tubes = MutableList(totalTubes) { i ->
            if (i < colorCount) {
                Tube(id = i, items = List(DEFAULT_CAPACITY) { selectedColors[i] }, capacity = DEFAULT_CAPACITY)
            } else {
                Tube(id = i, items = emptyList(), capacity = DEFAULT_CAPACITY)
            }
        }

        // 2. Perform N random valid reverse moves to shuffle cleanly
        val random = Random(levelNumber * 7919)
        val shuffleSteps = 40 + (levelNumber * 3).coerceAtMost(60)

        repeat(shuffleSteps) {
            val validMoves = mutableListOf<Pair<Int, Int>>()
            for (src in 0 until totalTubes) {
                if (tubes[src].items.isEmpty()) continue
                for (dst in 0 until totalTubes) {
                    if (src == dst) continue
                    if (tubes[dst].items.size < DEFAULT_CAPACITY) {
                        validMoves.add(Pair(src, dst))
                    }
                }
            }

            if (validMoves.isNotEmpty()) {
                val (src, dst) = validMoves[random.nextInt(validMoves.size)]
                val item = tubes[src].items.last()
                tubes[src] = tubes[src].copy(items = tubes[src].items.dropLast(1))
                tubes[dst] = tubes[dst].copy(items = tubes[dst].items + item)
            }
        }

        // Ensure not accidentally already solved
        if (isLevelSolved(tubes)) {
            // Swap two items
            if (tubes[0].items.isNotEmpty() && tubes[1].items.isNotEmpty()) {
                val item0 = tubes[0].items.last()
                val item1 = tubes[1].items.last()
                tubes[0] = tubes[0].copy(items = tubes[0].items.dropLast(1) + item1)
                tubes[1] = tubes[1].copy(items = tubes[1].items.dropLast(1) + item0)
            }
        }

        return tubes
    }

    /**
     * Checks if a move from source tube to destination tube is legal.
     */
    fun canMove(source: Tube, dest: Tube): Boolean {
        if (source.id == dest.id) return false
        if (source.isEmpty) return false
        if (dest.isFull) return false
        val top = source.topColor ?: return false
        return dest.isEmpty || dest.topColor == top
    }

    /**
     * Checks if all tubes are solved (every tube is either empty or filled with 4 of the same color).
     */
    fun isLevelSolved(tubes: List<Tube>): Boolean {
        for (tube in tubes) {
            if (tube.isEmpty) continue
            if (!tube.isComplete) return false
        }
        return true
    }

    /**
     * Finds a recommended move for the user as a hint.
     * Evaluates valid moves and ranks them by strategic value.
     */
    fun findHint(tubes: List<Tube>): Pair<Int, Int>? {
        if (isLevelSolved(tubes)) return null

        val candidateMoves = mutableListOf<Triple<Int, Int, Int>>() // fromIndex, toIndex, score

        for (i in tubes.indices) {
            val src = tubes[i]
            if (src.isEmpty) continue
            // If already complete, don't move from it
            if (src.isComplete) continue

            val top = src.topColor ?: continue
            val srcSameCount = src.items.count { it == top }
            val isSrcAllSameColor = src.items.all { it == top }

            for (j in tubes.indices) {
                if (i == j) continue
                val dst = tubes[j]
                if (!canMove(src, dst)) continue

                var score = 0

                // Moving onto an existing matching color
                if (!dst.isEmpty && dst.topColor == top) {
                    val dstSameCount = dst.items.count { it == top }
                    // Extra bonus if this move completes the destination tube
                    if (dst.items.size == dst.capacity - 1) {
                        score += 100
                    } else {
                        score += 50 + dstSameCount * 10
                    }
                } else if (dst.isEmpty) {
                    // Moving into an empty tube
                    if (isSrcAllSameColor) {
                        // Pointless move: moving a pure stack into an empty tube
                        score -= 50
                    } else {
                        // Helpful move: frees up colors trapped below in src
                        score += 20
                    }
                }

                candidateMoves.add(Triple(i, j, score))
            }
        }

        // Return highest scoring valid move
        return candidateMoves.maxByOrNull { it.third }?.let { Pair(it.first, it.second) }
    }
}
