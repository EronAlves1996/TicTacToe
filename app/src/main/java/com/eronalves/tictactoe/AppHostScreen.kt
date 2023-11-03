package com.eronalves.tictactoe

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import com.eronalves.tictactoe.ui.components.StartScreen

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
                StartScreen()
            }
            composable(TicTaeToeRoutes.Game.name) {}
            composable(TicTaeToeRoutes.GameHistory.name) {}
        }
    }
}


@Composable
@Preview(showBackground = true)
fun AppHostScreenPreview() {
    AppHostScreen()
}

