package com.eronalves.tictactoe

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

enum class TicTaeToeRoutes() {
    Start, Game, GameHistory
}

@Composable
fun AppHostScreen(
    navHostController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navHostController,
        startDestination = TicTaeToeRoutes.Start.name,
        modifier = modifier
    ) {
        composable(TicTaeToeRoutes.Start.name) {}
        composable(TicTaeToeRoutes.Game.name) {}
        composable(TicTaeToeRoutes.GameHistory.name) {}
    }
}