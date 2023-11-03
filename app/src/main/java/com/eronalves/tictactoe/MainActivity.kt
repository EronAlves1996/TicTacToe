package com.eronalves.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.eronalves.tictactoe.data.AppDatabase
import com.eronalves.tictactoe.ui.components.viewmodel.GlobalStateViewModel
import com.eronalves.tictactoe.ui.theme.TicTacToeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "tic-tac-toe"
        ).build()
        val viewModel: GlobalStateViewModel = GlobalStateViewModel(db)

        setContent {
            TicTacToeTheme {
                AppHostScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}