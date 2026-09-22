package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.data.PreferencesManager
import com.example.logic.GameEngine
import com.example.model.GameStatus
import com.example.model.MoveRecord
import com.example.model.Tube
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class GameUiState(
    val levelNumber: Int = 1,
    val tubes: List<Tube> = emptyList(),
    val selectedTubeIndex: Int? = null,
    val moveCount: Int = 0,
    val undoStack: List<MoveRecord> = emptyList(),
    val gameStatus: GameStatus = GameStatus.PLAYING,
    val hintMove: Pair<Int, Int>? = null, // fromIndex to toIndex
    val extraTubeUsed: Boolean = false,
    val message: String? = null,
    val maxUnlockedLevel: Int = 1,
    val bestMoves: Int = -1,
    val showVictoryDialog: Boolean = false,
    val showLevelSelectDialog: Boolean = false,
    val showHelpDialog: Boolean = false,
    val isDarkTheme: Boolean = true
)

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = PreferencesManager(application)

    private val _uiState = MutableStateFlow(
        GameUiState(
            levelNumber = prefs.currentLevel,
            maxUnlockedLevel = prefs.maxUnlockedLevel,
            isDarkTheme = prefs.isDarkTheme
        )
    )
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        loadLevel(_uiState.value.levelNumber)
    }

    fun loadLevel(levelNum: Int) {
        val initialTubes = GameEngine.createTubesForLevel(levelNum)
        val best = prefs.getBestMoves(levelNum)
        prefs.currentLevel = levelNum

        _uiState.update { current ->
            current.copy(
                levelNumber = levelNum,
                tubes = initialTubes,
                selectedTubeIndex = null,
                moveCount = 0,
                undoStack = emptyList(),
                gameStatus = GameStatus.PLAYING,
                hintMove = null,
                extraTubeUsed = false,
                message = null,
                bestMoves = best,
                showVictoryDialog = false,
                showLevelSelectDialog = false
            )
        }
    }

    fun onTubeClicked(tubeIndex: Int) {
        val state = _uiState.value
        if (state.gameStatus == GameStatus.WON) return

        val selected = state.selectedTubeIndex

        if (selected == null) {
            // First tap: attempt to select source tube
            val tube = state.tubes.getOrNull(tubeIndex) ?: return
            if (tube.isEmpty) {
                _uiState.update { it.copy(message = "Tap a container with colors to pick up 🧪") }
                return
            }
            if (tube.isComplete) {
                _uiState.update { it.copy(message = "This container is already sorted! 🎉") }
                return
            }
            _uiState.update {
                it.copy(
                    selectedTubeIndex = tubeIndex,
                    hintMove = null,
                    message = null
                )
            }
        } else if (selected == tubeIndex) {
            // Tap same tube: deselect
            _uiState.update {
                it.copy(selectedTubeIndex = null, message = null)
            }
        } else {
            // Second tap: attempt to pour into destination tube
            val sourceTube = state.tubes.getOrNull(selected) ?: return
            val destTube = state.tubes.getOrNull(tubeIndex) ?: return

            if (GameEngine.canMove(sourceTube, destTube)) {
                // Execute move
                val item = sourceTube.items.last()
                val newSourceItems = sourceTube.items.dropLast(1)
                val newDestItems = destTube.items + item

                val updatedTubes = state.tubes.toMutableList()
                updatedTubes[selected] = sourceTube.copy(items = newSourceItems)
                updatedTubes[tubeIndex] = destTube.copy(items = newDestItems)

                val newUndoStack = state.undoStack + MoveRecord(selected, tubeIndex, item)
                val newMoveCount = state.moveCount + 1

                val isWon = GameEngine.isLevelSolved(updatedTubes)

                if (isWon) {
                    prefs.saveBestMoves(state.levelNumber, newMoveCount)
                    val nextUnlocked = (state.levelNumber + 1).coerceAtLeast(state.maxUnlockedLevel)
                    prefs.maxUnlockedLevel = nextUnlocked

                    _uiState.update {
                        it.copy(
                            tubes = updatedTubes,
                            selectedTubeIndex = null,
                            moveCount = newMoveCount,
                            undoStack = newUndoStack,
                            gameStatus = GameStatus.WON,
                            showVictoryDialog = true,
                            hintMove = null,
                            message = "Level Complete! 🎉",
                            maxUnlockedLevel = nextUnlocked,
                            bestMoves = prefs.getBestMoves(state.levelNumber)
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            tubes = updatedTubes,
                            selectedTubeIndex = null,
                            moveCount = newMoveCount,
                            undoStack = newUndoStack,
                            hintMove = null,
                            message = null
                        )
                    }
                }
            } else {
                // Cannot move.
                // If destination has items and is not complete, switch selection to destination!
                if (!destTube.isEmpty && !destTube.isComplete) {
                    _uiState.update {
                        it.copy(selectedTubeIndex = tubeIndex, hintMove = null, message = null)
                    }
                } else if (destTube.isFull) {
                    _uiState.update { it.copy(message = "Container is full! 🛑") }
                } else {
                    _uiState.update { it.copy(message = "Colors must match to stack! ⚠️") }
                }
            }
        }
    }

    fun undo() {
        val state = _uiState.value
        if (state.undoStack.isEmpty() || state.gameStatus == GameStatus.WON) return

        val lastMove = state.undoStack.last()
        val newUndoStack = state.undoStack.dropLast(1)

        val updatedTubes = state.tubes.toMutableList()
        val fromTube = updatedTubes[lastMove.fromTubeIndex]
        val toTube = updatedTubes[lastMove.toTubeIndex]

        // Reverse: take top item from toTube and put it back onto fromTube
        val item = toTube.items.lastOrNull() ?: return
        updatedTubes[lastMove.toTubeIndex] = toTube.copy(items = toTube.items.dropLast(1))
        updatedTubes[lastMove.fromTubeIndex] = fromTube.copy(items = fromTube.items + item)

        _uiState.update {
            it.copy(
                tubes = updatedTubes,
                selectedTubeIndex = null,
                undoStack = newUndoStack,
                hintMove = null,
                message = "Move undone ↩️"
            )
        }
    }

    fun restartLevel() {
        loadLevel(_uiState.value.levelNumber)
    }

    fun addExtraTube() {
        val state = _uiState.value
        if (state.extraTubeUsed || state.gameStatus == GameStatus.WON) return

        val newTubeId = state.tubes.size
        val extraTube = Tube(id = newTubeId, items = emptyList(), capacity = GameEngine.DEFAULT_CAPACITY)
        val updatedTubes = state.tubes + extraTube

        _uiState.update {
            it.copy(
                tubes = updatedTubes,
                extraTubeUsed = true,
                message = "Extra empty container added! 🧪✨"
            )
        }
    }

    fun getHint() {
        val state = _uiState.value
        if (state.gameStatus == GameStatus.WON) return

        val hint = GameEngine.findHint(state.tubes)
        if (hint != null) {
            _uiState.update {
                it.copy(
                    hintMove = hint,
                    selectedTubeIndex = hint.first,
                    message = "💡 Pour from Tube #${hint.first + 1} to Tube #${hint.second + 1}"
                )
            }
        } else {
            _uiState.update {
                it.copy(message = "No direct moves found! Try ↩️ Undo or +1 🧪")
            }
        }
    }

    fun nextLevel() {
        loadLevel(_uiState.value.levelNumber + 1)
    }

    fun selectLevel(levelNum: Int) {
        loadLevel(levelNum)
    }

    fun toggleTheme() {
        val newTheme = !_uiState.value.isDarkTheme
        prefs.isDarkTheme = newTheme
        _uiState.update { it.copy(isDarkTheme = newTheme) }
    }

    fun showLevelSelectDialog(show: Boolean) {
        _uiState.update { it.copy(showLevelSelectDialog = show) }
    }

    fun showHelpDialog(show: Boolean) {
        _uiState.update { it.copy(showHelpDialog = show) }
    }

    fun dismissVictoryDialog() {
        _uiState.update { it.copy(showVictoryDialog = false) }
    }

    fun clearMessage() {
        _uiState.update { it.copy(message = null) }
    }
}
