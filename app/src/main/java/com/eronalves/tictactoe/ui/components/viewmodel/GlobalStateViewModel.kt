package com.eronalves.tictactoe.ui.components.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Arrays
import java.util.stream.Collectors
import java.util.stream.IntStream

enum class CellStates {
    Empty, X, O
}

enum class PlayerTime {
    Player1, Player2
}

enum class Winner {
    Player1, Player2, Match, NoWinner
}

data class GameState(
    val player1Name: String? = null,
    val player2Name: String? = null,
    val isRobotEnabled: Boolean = false,
    val tableSize: Int = 3,
    val gameTable: Array<Array<CellStates>>? = null,
    val playerTime: PlayerTime = PlayerTime.Player1,
    val winner: Winner = Winner.NoWinner
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
            val hasWinner = checkIfGameEnded(table, it.tableSize)

            it.copy(
                gameTable = table,
                playerTime = swapPlaying(it.playerTime),
                winner = hasWinner
            )
        }
    }

    suspend fun makeRobotPlay() {
        uiState.collect { state ->
            val table = state.gameTable

            val emptyIndices = IntStream.range(0, table!!.size).mapToObj { i -> Pair(i, null) }
                .flatMap { i ->
                    IntStream.range(0, table[i.first].size).mapToObj { Pair(i.first, it) }
                }.filter { table[it.first][it.second] == CellStates.Empty }
                .collect(Collectors.toList())

            val robotPlaying = emptyIndices[(0 until emptyIndices.size).random()]

            delay(1000)
            makePlay(robotPlaying.first, robotPlaying.second)
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

    private fun checkIfGameEnded(table: Array<Array<CellStates>>?, tableSize: Int): Winner {
        for (i in 0..tableSize - 1) {
            val lineWinner = checkIfLineHasWinner(table, i)
            if (lineWinner != Winner.NoWinner) return lineWinner
            val columnWinner = checkIfColumnHasWinner(table, i)
            if (columnWinner != Winner.NoWinner) return columnWinner
        }
        val diagonalWinner = checkIfDiagonalsHasWinner(table, tableSize)
        if (diagonalWinner != Winner.NoWinner) return diagonalWinner
        val isAMatch = checkIfMatch(table)
        if (isAMatch != Winner.NoWinner) return isAMatch

        return Winner.NoWinner
    }

    private fun checkIfLineHasWinner(table: Array<Array<CellStates>>?, lineNumber: Int): Winner {
        val tableLine = table?.get(lineNumber)
        var actualWinner = Winner.NoWinner

        for (i in tableLine!!.indices) {
            val cellState = tableLine[i]
            if (cellState == CellStates.Empty) return Winner.NoWinner

            if (actualWinner == Winner.NoWinner) {
                actualWinner = assignWinner(cellState)
            } else {
                when (actualWinner) {
                    Winner.Player1 -> if (cellState != CellStates.X) return Winner.NoWinner
                    Winner.Player2 -> if (cellState != CellStates.O) return Winner.NoWinner
                    else -> continue
                }
            }
        }

        return actualWinner
    }

    private fun assignWinner(cell: CellStates): Winner {
        return when (cell) {
            CellStates.X -> Winner.Player1
            CellStates.O -> Winner.Player2
            else -> Winner.NoWinner
        }
    }

    private fun checkIfColumnHasWinner(
        table: Array<Array<CellStates>>?,
        columnNumber: Int
    ): Winner {
        var actualWinner = Winner.NoWinner

        for (i in table!!.indices) {
            val cellState = table[i][columnNumber]
            if (cellState == CellStates.Empty) return Winner.NoWinner

            if (actualWinner == Winner.NoWinner) {
                actualWinner = assignWinner(cellState)
            } else {
                when (actualWinner) {
                    Winner.Player1 -> if (cellState != CellStates.X) return Winner.NoWinner
                    Winner.Player2 -> if (cellState != CellStates.O) return Winner.NoWinner
                    else -> continue
                }
            }
        }

        return actualWinner
    }

    private fun checkIfDiagonalsHasWinner(
        table: Array<Array<CellStates>>?,
        tableSize: Int
    ): Winner {
        var actualWinner = Winner.NoWinner
        val tableLength = tableSize - 1

        for (i in 0..tableLength) {
            val cellState = table!![i][i]

            if (cellState == CellStates.Empty) {
                actualWinner = Winner.NoWinner
                break
            }

            if (actualWinner == Winner.NoWinner) {
                actualWinner = assignWinner(cellState)
            } else {
                when (actualWinner) {
                    Winner.Player1 -> if (cellState != CellStates.X) {
                        actualWinner = Winner.NoWinner
                        break
                    }

                    Winner.Player2 -> if (cellState != CellStates.O) {
                        actualWinner = Winner.NoWinner
                        break
                    }

                    else -> continue
                }
            }
        }

        if (actualWinner != Winner.NoWinner) return actualWinner

        for (i in 0..tableLength) {
            val cellState = table!![i][table[i].size - 1 - i]

            if (cellState == CellStates.Empty) return Winner.NoWinner

            if (actualWinner == Winner.NoWinner) {
                actualWinner = assignWinner(cellState)
            } else {
                when (actualWinner) {
                    Winner.Player1 -> if (cellState != CellStates.X) {
                        actualWinner = Winner.NoWinner
                        break
                    }

                    Winner.Player2 -> if (cellState != CellStates.O) {
                        actualWinner = Winner.NoWinner
                        break
                    }

                    else -> continue
                }
            }
        }

        return actualWinner
    }

    private fun checkIfMatch(table: Array<Array<CellStates>>?): Winner {
        val totalOfEmpties = Arrays
            .stream(table)
            .map { line -> Arrays.stream(line).filter { item -> item == CellStates.Empty }.count() }
            .reduce(0) { totalOfEmpties, emptyLineCount -> totalOfEmpties + emptyLineCount }
        if (totalOfEmpties > 0) return Winner.NoWinner
        return Winner.Match
    }
}