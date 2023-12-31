package com.eronalves.tictactoe

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eronalves.tictactoe.ui.components.viewmodel.GlobalStateViewModel
import com.eronalves.tictactoe.ui.components.StartScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eronalves.tictactoe.data.AppDatabase
import com.eronalves.tictactoe.data.LastFirstPlayer
import com.eronalves.tictactoe.ui.components.GameScreen
import com.eronalves.tictactoe.ui.components.screens.HistoryScreen
import com.eronalves.tictactoe.ui.components.viewmodel.Winner
import kotlinx.coroutines.launch

enum class TicTaeToeRoutes() {
    Start, Game, GameHistory
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TicTacToeTopBar(modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
            Row {
                Text(
                    stringResource(id = R.string.app_name),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHostScreen(
    navHostController: NavHostController = rememberNavController(),
    viewModel: GlobalStateViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(topBar = {
        TicTacToeTopBar()
    }, modifier = modifier) {
        NavHost(
            navController = navHostController,
            startDestination = TicTaeToeRoutes.Start.name,
            modifier = Modifier.padding(it)
        ) {
            composable(TicTaeToeRoutes.Start.name) {
                StartScreen(
                    persistedFirstName = uiState.player1Name ?: "",
                    onLoadHistory = { navHostController.navigate(TicTaeToeRoutes.GameHistory.name) },
                    onSaveLastPlayer1Name = { name ->
                        viewModel.saveLastFirstPlayerName(name)
                    },
                    onStartGame = { player1Name, player2Name, isRobotEnabled, selectedTableSize ->
                        viewModel.startGame(
                            player1Name,
                            player2Name,
                            isRobotEnabled,
                            selectedTableSize
                        )
                        navHostController.navigate(TicTaeToeRoutes.Game.name)
                    },
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(10.dp)
                        .verticalScroll(rememberScrollState())
                )

            }
            composable(TicTaeToeRoutes.Game.name) {
                GameScreen(
                    viewModel, modifier = Modifier
                        .padding(10.dp)
                        .fillMaxHeight(),
                    onNewGame = {
                        viewModel.saveUnfinishedGameInfo()
                        navHostController.popBackStack(TicTaeToeRoutes.Start.name, false)
                    }
                )
            }
            composable(TicTaeToeRoutes.GameHistory.name) {
                HistoryScreen(viewModel = viewModel, onClickBack = {
                    navHostController.popBackStack(TicTaeToeRoutes.Start.name, false)
                }, modifier = Modifier
                    .padding(10.dp)
                    .fillMaxHeight())
            }
        }
    }
}
