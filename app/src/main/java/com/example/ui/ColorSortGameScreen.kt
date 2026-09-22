package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.Tube
import com.example.ui.components.GameControls
import com.example.ui.components.GameHeader
import com.example.ui.components.GlassTube
import com.example.ui.components.HelpDialog
import com.example.ui.components.LevelSelectDialog
import com.example.ui.components.VictoryDialog
import com.example.ui.viewmodel.GameViewModel
import kotlinx.coroutines.delay

@Composable
fun ColorSortGameScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val screenBg = if (uiState.isDarkTheme) Color(0xFF0B0F19) else Color(0xFFF8FAFC)
    val shelfColor = if (uiState.isDarkTheme) Color(0xFF1E293B) else Color(0xFFCBD5E1)

    // Auto-clear transient feedback message after 2.5 seconds
    LaunchedEffect(uiState.message) {
        if (uiState.message != null && !uiState.showVictoryDialog) {
            delay(2500)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(screenBg),
        containerColor = screenBg
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Header Card
                GameHeader(
                    levelNumber = uiState.levelNumber,
                    moveCount = uiState.moveCount,
                    bestMoves = uiState.bestMoves,
                    isDarkTheme = uiState.isDarkTheme,
                    onOpenLevelSelect = { viewModel.showLevelSelectDialog(true) },
                    onOpenHelp = { viewModel.showHelpDialog(true) },
                    onToggleTheme = { viewModel.toggleTheme() }
                )

                // Instruction & Message Pill
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val msg = uiState.message
                    if (msg != null) {
                        Surface(
                            shape = RoundedCornerShape(19.dp),
                            color = if (uiState.isDarkTheme) Color(0xFF1E293B) else Color(0xFFE2E8F0),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (uiState.isDarkTheme) Color(0xFF3B82F6) else Color(0xFF60A5FA)
                            )
                        ) {
                            Text(
                                text = msg,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (uiState.isDarkTheme) Color(0xFF93C5FD) else Color(0xFF1D4ED8),
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        Text(
                            text = if (uiState.selectedTubeIndex == null) {
                                "Tap a glass tube to pick up the top color"
                            } else {
                                "Tap another tube to pour color into it"
                            },
                            fontSize = 13.sp,
                            color = if (uiState.isDarkTheme) Color(0xFF64748B) else Color(0xFF94A3B8),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Middle: The Laboratory Shelves with Glass Tubes
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val tubes = uiState.tubes
                    val totalTubes = tubes.size

                    if (totalTubes <= 4) {
                        // Single row for 3-4 tubes
                        TubeShelfRow(
                            tubes = tubes,
                            startIndex = 0,
                            selectedTubeIndex = uiState.selectedTubeIndex,
                            hintMove = uiState.hintMove,
                            isDarkTheme = uiState.isDarkTheme,
                            shelfColor = shelfColor,
                            onTubeClicked = { viewModel.onTubeClicked(it) }
                        )
                    } else {
                        // Two rows for 5+ tubes
                        val splitIndex = (totalTubes + 1) / 2
                        val topRowTubes = tubes.take(splitIndex)
                        val bottomRowTubes = tubes.drop(splitIndex)

                        TubeShelfRow(
                            tubes = topRowTubes,
                            startIndex = 0,
                            selectedTubeIndex = uiState.selectedTubeIndex,
                            hintMove = uiState.hintMove,
                            isDarkTheme = uiState.isDarkTheme,
                            shelfColor = shelfColor,
                            onTubeClicked = { viewModel.onTubeClicked(it) }
                        )

                        TubeShelfRow(
                            tubes = bottomRowTubes,
                            startIndex = splitIndex,
                            selectedTubeIndex = uiState.selectedTubeIndex,
                            hintMove = uiState.hintMove,
                            isDarkTheme = uiState.isDarkTheme,
                            shelfColor = shelfColor,
                            onTubeClicked = { viewModel.onTubeClicked(it) }
                        )
                    }
                }

                // Bottom Game Controls Bar
                GameControls(
                    canUndo = uiState.undoStack.isNotEmpty(),
                    extraTubeUsed = uiState.extraTubeUsed,
                    isDarkTheme = uiState.isDarkTheme,
                    onUndo = { viewModel.undo() },
                    onRestart = { viewModel.restartLevel() },
                    onHint = { viewModel.getHint() },
                    onAddExtraTube = { viewModel.addExtraTube() },
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            // Dialogs
            if (uiState.showVictoryDialog) {
                VictoryDialog(
                    levelNumber = uiState.levelNumber,
                    movesTaken = uiState.moveCount,
                    bestMoves = uiState.bestMoves,
                    isDarkTheme = uiState.isDarkTheme,
                    onNextLevel = {
                        viewModel.dismissVictoryDialog()
                        viewModel.nextLevel()
                    },
                    onReplay = {
                        viewModel.dismissVictoryDialog()
                        viewModel.restartLevel()
                    },
                    onDismiss = { viewModel.dismissVictoryDialog() }
                )
            }

            if (uiState.showLevelSelectDialog) {
                LevelSelectDialog(
                    currentLevel = uiState.levelNumber,
                    maxUnlockedLevel = uiState.maxUnlockedLevel,
                    isDarkTheme = uiState.isDarkTheme,
                    onSelectLevel = { viewModel.selectLevel(it) },
                    onDismiss = { viewModel.showLevelSelectDialog(false) }
                )
            }

            if (uiState.showHelpDialog) {
                HelpDialog(
                    isDarkTheme = uiState.isDarkTheme,
                    onDismiss = { viewModel.showHelpDialog(false) }
                )
            }
        }
    }
}

@Composable
private fun TubeShelfRow(
    tubes: List<Tube>,
    startIndex: Int,
    selectedTubeIndex: Int?,
    hintMove: Pair<Int, Int>?,
    isDarkTheme: Boolean,
    shelfColor: Color,
    onTubeClicked: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Tubes Row
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 8.dp)
        ) {
            tubes.forEachIndexed { relativeIndex, tube ->
                val actualIndex = startIndex + relativeIndex
                val isSelected = selectedTubeIndex == actualIndex
                val isHintSource = hintMove?.first == actualIndex
                val isHintTarget = hintMove?.second == actualIndex

                GlassTube(
                    tube = tube,
                    isSelected = isSelected,
                    isHintSource = isHintSource,
                    isHintTarget = isHintTarget,
                    isDarkTheme = isDarkTheme,
                    onClick = { onTubeClicked(actualIndex) }
                )
            }
        }

        // Flat wooden/slate laboratory shelf rack beneath the tubes
        Box(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(shelfColor)
        )
    }
}
