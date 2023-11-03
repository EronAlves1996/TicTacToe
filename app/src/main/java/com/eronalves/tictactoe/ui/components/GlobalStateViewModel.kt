package com.eronalves.tictactoe.ui.components

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class GameState(
    val player1Name: String? = null,
    val player2Name: String? = null,
    val isRobotEnabled: Boolean = false,
    val tableSize: Int = 3,
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
                tableSize = tableSize
            )
        }
    }
}