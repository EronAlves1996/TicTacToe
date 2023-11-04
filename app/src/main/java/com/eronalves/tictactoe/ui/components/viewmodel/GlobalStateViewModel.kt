package com.eronalves.tictactoe.ui.components.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eronalves.tictactoe.data.AppDatabase
import com.eronalves.tictactoe.data.LastFirstPlayer
import com.eronalves.tictactoe.data.Move
import com.eronalves.tictactoe.data.Player
import com.eronalves.tictactoe.data.Round
import com.eronalves.tictactoe.data.RoundAndPlayers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Arrays
import java.util.Date
import java.util.stream.Collectors
import java.util.stream.IntStream
import kotlin.math.ceil
import kotlin.streams.toList

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

data class RoundListInfo(
    val itemCount: List<Int>? = null,
    val roundList: List<RoundAndPlayers>? = null
)


class GlobalStateViewModel(db: AppDatabase) : ViewModel() {
    private val _uiState = MutableStateFlow(GameState())
    private val lastFirstPlayerDao = db.lastFirstPlayerDao()
    private val playerDao = db.playerDao()
    private val roundDao = db.roundDao()
    private val moveDao = db.moveDao()
    private var round: Round? = Round(null, null, null, null, null, null)
    private var moves: ArrayList<Move> = ArrayList()
    private val _roundItemCountState = MutableStateFlow(RoundListInfo())

    val uiState = _uiState.asStateFlow()
    val roundItemCountState = _roundItemCountState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val lastFirstPlayer = lastFirstPlayerDao.checkIfHaveAFirstPlayer()
            if (lastFirstPlayer != null) _uiState.update {
                it.copy(player1Name = lastFirstPlayer.name)
            }
        }
    }

    fun startGame(
        player1Name: String,
        player2Name: String,
        isRobotEnabled: Boolean,
        tableSize: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            round = Round(
                null,
                tableSize,
                resolvePlayerId(player1Name),
                resolvePlayerId(player2Name),
                null,
                Date()
            )
        }
        moves = ArrayList()

        _uiState.update { currentState ->
            currentState.copy(
                player1Name = player1Name,
                player2Name = player2Name,
                isRobotEnabled = isRobotEnabled,
                tableSize = tableSize,
                gameTable = Array(tableSize) { Array(tableSize) { CellStates.Empty } },
                playerTime = PlayerTime.Player1,
                winner = Winner.NoWinner
            )
        }
    }

    fun saveLastFirstPlayerName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val lastFirstPlayer = lastFirstPlayerDao.checkIfHaveAFirstPlayer()
            if (lastFirstPlayer == null) {
                lastFirstPlayerDao.insert(LastFirstPlayer(1, name))
                return@launch
            }
            lastFirstPlayerDao.updateLastFirstPlayer(lastFirstPlayer.copy(name = name))
        }
    }

    fun makePlay(x: Int, y: Int) {
        var gameHasWinner = false
        _uiState.update {
            it?.gameTable?.get(x)?.set(y, associatePlayerToCellSymbol(it.playerTime))
            val table = it.gameTable
            val hasWinner = checkIfGameEnded(table, it.tableSize)
            moves.add(
                Move(
                    null, x, y, if (it.playerTime == PlayerTime.Player1) {
                        round?.player1Id
                    } else {
                        round?.player2Id
                    },
                    null
                )
            )

            if (hasWinner != Winner.NoWinner) {
                gameHasWinner = true
            }

            it.copy(
                gameTable = table,
                playerTime = swapPlaying(it.playerTime),
                winner = hasWinner
            )
        }

        if (gameHasWinner) saveEndedGameInformation()
    }

    fun saveUnfinishedGameInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val state = _uiState.value
            if (state.winner == Winner.NoWinner) {
                val roundId = roundDao.create(round!!)
                val savedMoves = moves.map { move -> move.copy(roundId = roundId) }
                moveDao.insertAll(*savedMoves.map { m -> m }.toTypedArray())
            }
        }

    }

    fun getListedRoundPages() {
        viewModelScope.launch(Dispatchers.IO) {
            val itemCount = roundDao.getItemCount()
            val pageCount = ceil(itemCount / 6.0).toInt()

            _roundItemCountState.update {
                it.copy(itemCount = IntStream.range(1, pageCount + 1).toList())
            }
        }
    }

    fun getHistoryPaginatedFrom(start: Int) {
        val translatedStart = start * 6
        viewModelScope.launch(Dispatchers.IO) {
            val listedItems = roundDao.getPaginatedFrom(translatedStart)
            _roundItemCountState.update {
                it.copy(
                    roundList = listedItems
                )
            }
        }
    }

    private suspend fun resolvePlayerId(name: String): Long? {
        val player = playerDao.findByName(name)

        if (player != null) return player.id

        return playerDao.create(Player(id = null, name = name))
    }

    private fun saveEndedGameInformation() {
        viewModelScope.launch(Dispatchers.IO) {
            val state = _uiState.value
            if (state.winner != Winner.NoWinner) {
                val savedRound = round?.copy(
                    winner = when (state.winner) {
                        Winner.Match -> com.eronalves.tictactoe.data.Winner.Match
                        Winner.Player1 -> com.eronalves.tictactoe.data.Winner.Player1
                        else -> com.eronalves.tictactoe.data.Winner.Player2
                    },
                )
                val roundId = roundDao.create(savedRound!!)
                val savedMoves =
                    moves.stream().map { move -> move.copy(roundId = roundId) }
                        .collect(Collectors.toList())
                moveDao.insertAll(*savedMoves.map { item -> item }.toTypedArray())
            }
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