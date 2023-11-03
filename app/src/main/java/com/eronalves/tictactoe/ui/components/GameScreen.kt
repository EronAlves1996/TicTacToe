package com.eronalves.tictactoe.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eronalves.tictactoe.R
import com.eronalves.tictactoe.ui.components.viewmodel.CellStates
import com.eronalves.tictactoe.ui.components.viewmodel.GameState
import com.eronalves.tictactoe.ui.components.viewmodel.GlobalStateViewModel
import com.eronalves.tictactoe.ui.components.viewmodel.PlayerTime
import com.eronalves.tictactoe.ui.components.viewmodel.Winner
import com.eronalves.tictactoe.ui.theme.player1Color
import com.eronalves.tictactoe.ui.theme.player2Color

data class CellState(val state: String, val color: Color)

@Composable
fun Cell(
    state: CellStates,
    hasWinner: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isRobotEnabled: Boolean,
    isRobotTime: Boolean
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        enabled = (state == CellStates.Empty && !hasWinner) && !(isRobotTime && isRobotEnabled),
        shape = RectangleShape,
        border = BorderStroke(
            1.dp, MaterialTheme.colorScheme.onSecondaryContainer
        ),
        modifier = modifier
    ) {
        val cellState = when (state) {
            CellStates.Empty -> CellState("", Color.Transparent)
            else -> CellState(
                state.name, when (state) {
                    CellStates.X -> MaterialTheme.colorScheme.player1Color
                    else -> MaterialTheme.colorScheme.player2Color
                }
            )
        }

        Text(
            text = cellState.state,
            color = cellState.color,
            style = MaterialTheme.typography.displayMedium
        )
    }
}

data class AgreggateDataGameState(
    val playerTime: PlayerTime,
    @StringRes val topTextContent: Int,
    val bellowTextColor: Color,
    val bellowTextContent: String
)

@Composable
fun GameStateIndicator(globalUiState: GameState, modifier: Modifier = Modifier) {
    val playerTime = globalUiState.playerTime
    val player1Name = globalUiState.player1Name
    val player2Name = globalUiState.player2Name
    val agreggateState = when (globalUiState.winner) {
        Winner.NoWinner -> (if (globalUiState.playerTime == PlayerTime.Player1) {
            player1Name
        } else {
            player2Name
        })?.let {
            AgreggateDataGameState(
                playerTime,
                R.string.player_time,
                if (globalUiState.playerTime == PlayerTime.Player1) {
                    MaterialTheme.colorScheme.player1Color
                } else {
                    MaterialTheme.colorScheme.player2Color
                },
                it
            )
        }

        Winner.Match -> AgreggateDataGameState(
            playerTime, R.string.its_a_match, if (globalUiState.playerTime == PlayerTime.Player1) {
                MaterialTheme.colorScheme.player1Color
            } else {
                MaterialTheme.colorScheme.player2Color
            }, ""
        )

        else -> (if (globalUiState.winner == Winner.Player1) {
            globalUiState.player1Name
        } else {
            globalUiState.player2Name
        })?.let {
            AgreggateDataGameState(
                playerTime, R.string.has_winner, if (globalUiState.winner == Winner.Player1) {
                    MaterialTheme.colorScheme.player1Color
                } else {
                    MaterialTheme.colorScheme.player2Color
                }, it
            )
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier
    ) {
        if (agreggateState != null) {
            Text(
                text = stringResource(id = agreggateState.topTextContent),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = agreggateState.bellowTextContent,
                color = agreggateState.bellowTextColor,
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}


@Composable
fun GameScreen(
    viewModel: GlobalStateViewModel, modifier: Modifier = Modifier
) {
    val globalUiState by viewModel.uiState.collectAsState()
    val openRobotPlayingTime =
        globalUiState.isRobotEnabled && globalUiState.playerTime == PlayerTime.Player2 && globalUiState.winner == Winner.NoWinner

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (openRobotPlayingTime) {
            LaunchedEffect(key1 = "Popup") {
                viewModel.makeRobotPlay()
            }
            AlertDialog(onDismissRequest = { /*TODO*/ }, confirmButton = { /*TODO*/ }, text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "É a vez do robô fazer sua jogada",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(text = "O robô está analisando...")
                }
            })
        }

        GameStateIndicator(
            globalUiState = globalUiState, modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .width(400.dp)
                .height(400.dp)
        ) {
            for (i in globalUiState.gameTable!!.indices) {
                Row(Modifier.weight(0.5F, fill = true)) {
                    for (j in globalUiState.gameTable!!.get(i).indices) {
                        val cell = globalUiState.gameTable!![i][j]
                        Cell(
                            state = cell,
                            onClick = {
                                viewModel.makePlay(i, j)
                            },
                            isRobotEnabled = globalUiState.isRobotEnabled,
                            isRobotTime = globalUiState.playerTime == PlayerTime.Player2,
                            hasWinner = globalUiState.winner != Winner.NoWinner,
                            modifier = Modifier
                                .weight(1.0F, fill = true)
                                .fillMaxHeight()
                        )
                    }
                }
            }
        }
        BottomControls(primaryButtonLabel = R.string.restart_game,
            primaryButtonOnClick = { /*TODO*/ },
            primaryButtonEnabled = true,
            secondaryButtonLabel = R.string.new_game,
            secondaryButtonOnClick = { /*TODO*/ })
    }
}