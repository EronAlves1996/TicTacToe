package com.eronalves.tictactoe.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.eronalves.tictactoe.R
import com.eronalves.tictactoe.ui.components.viewmodel.CellStates
import com.eronalves.tictactoe.ui.components.viewmodel.GlobalStateViewModel
import com.eronalves.tictactoe.ui.components.viewmodel.PlayerTime
import com.eronalves.tictactoe.ui.theme.player1Color
import com.eronalves.tictactoe.ui.theme.player2Color

data class CellState(val state: String, val color: Color)

@Composable
fun Cell(state: CellStates, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        enabled = state == CellStates.Empty,
        shape = RectangleShape,
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.onSecondaryContainer
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

@Composable
fun GameScreen(
    viewModel: GlobalStateViewModel, modifier: Modifier = Modifier
) {
    val globalUiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            Text(text = "Vez do Jogador", style = MaterialTheme.typography.titleLarge)
            Text(
                text = if (globalUiState.playerTime == PlayerTime.Player1) {
                    globalUiState.player1Name!!
                } else {
                    globalUiState.player2Name!!
                },
                color = if (globalUiState.playerTime == PlayerTime.Player1) {
                    MaterialTheme.colorScheme.player1Color
                } else {
                    MaterialTheme.colorScheme.player2Color
                },
                style = MaterialTheme.typography.titleLarge,
            )
        }
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