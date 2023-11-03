package com.eronalves.tictactoe

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eronalves.tictactoe.ui.components.GlobalStateViewModel
import com.eronalves.tictactoe.ui.components.StartScreen
import androidx.lifecycle.viewmodel.compose.viewModel

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
    viewModel: GlobalStateViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
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
                    onLoadHistory = { navHostController.navigate(TicTaeToeRoutes.GameHistory.name) },
                    onStartGame = { player1Name, player2Name, isRobotEnabled, selectedTableSize ->
                        viewModel.startGame(
                            player1Name,
                            player2Name,
                            isRobotEnabled,
                            selectedTableSize
                        )
                        navHostController.navigate(TicTaeToeRoutes.Game.name)
                    })
            }
            composable(TicTaeToeRoutes.Game.name) {}
            composable(TicTaeToeRoutes.GameHistory.name) {}
        }
    }
}


@Composable
@Preview(showBackground = true)
fun AppHostScreenPreview() {
    AppHostScreen(
        modifier = Modifier
            .fillMaxHeight()
            .wrapContentSize()
    )
}

