package com.example.data

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("color_sort_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_CURRENT_LEVEL = "current_level"
        private const val KEY_MAX_UNLOCKED = "max_unlocked_level"
        private const val KEY_SOUND = "sound_enabled"
        private const val KEY_DARK_THEME = "dark_theme"
        private const val PREFIX_BEST_MOVES = "best_moves_level_"
    }

    var currentLevel: Int
        get() = prefs.getInt(KEY_CURRENT_LEVEL, 1)
        set(value) = prefs.edit().putInt(KEY_CURRENT_LEVEL, value).apply()

    var maxUnlockedLevel: Int
        get() = prefs.getInt(KEY_MAX_UNLOCKED, 1)
        set(value) = prefs.edit().putInt(KEY_MAX_UNLOCKED, value.coerceAtLeast(maxUnlockedLevel)).apply()

    var isSoundEnabled: Boolean
        get() = prefs.getBoolean(KEY_SOUND, true)
        set(value) = prefs.edit().putBoolean(KEY_SOUND, value).apply()

    var isDarkTheme: Boolean
        get() = prefs.getBoolean(KEY_DARK_THEME, true)
        set(value) = prefs.edit().putBoolean(KEY_DARK_THEME, value).apply()

    fun getBestMoves(level: Int): Int {
        return prefs.getInt(PREFIX_BEST_MOVES + level, -1)
    }

    fun saveBestMoves(level: Int, moves: Int) {
        val currentBest = getBestMoves(level)
        if (currentBest == -1 || moves < currentBest) {
            prefs.edit().putInt(PREFIX_BEST_MOVES + level, moves).apply()
        }
    }
}
