package com.eronalves.tictactoe.ui.components.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class CellStates {
    Empty, X, O
}

enum class PlayerTime {
    Player1, Player2
}

data class GameState(
    val player1Name: String? = null,
    val player2Name: String? = null,
    val isRobotEnabled: Boolean = false,
    val tableSize: Int = 3,
    val gameTable: Array<Array<CellStates>>? = null,
    val playerTime: PlayerTime = PlayerTime.Player1
)


class GlobalStateViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameState())
    val uiState = _uiState.asStateFlow()

    fun startGame(
        player1Name: String,
        player2Name: String,
        isRobotEnabled: Boolean,
        tableSize: Int
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                player1Name = player1Name,
                player2Name = player2Name,
                isRobotEnabled = isRobotEnabled,
                tableSize = tableSize,
                gameTable = Array(tableSize) { Array(tableSize) { CellStates.Empty } },
                playerTime = PlayerTime.Player1
            )
        }
    }

    fun makePlay(x: Int, y: Int) {
        _uiState.update {
            it?.gameTable?.get(x)?.set(y, associatePlayerToCellSymbol(it.playerTime))
            val table = it.gameTable

            it.copy(
                gameTable = table,
                playerTime = swapPlaying(it.playerTime)
            )
        }
    }

    private fun associatePlayerToCellSymbol(player: PlayerTime): CellStates {
        return when (player) {
            PlayerTime.Player1 -> CellStates.X
            else -> CellStates.O
        }
    }

    private fun swapPlaying(player: PlayerTime): PlayerTime {
        return when (player) {
            PlayerTime.Player1 -> PlayerTime.Player2
            else -> PlayerTime.Player1
        }
    }
}