package com.eronalves.tictactoe.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.eronalves.tictactoe.R
import com.eronalves.tictactoe.ui.theme.light_Player1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen() {
    Column {
        Image(
            painter = painterResource(
                id = if (isSystemInDarkTheme()) {
                    R.drawable.app_logo_dark
                } else {
                    R.drawable.app_logo_light
                }
            ),
            contentDescription = "Logotipo Jogo da Velha"
        )
        Text(text = "Tipo de Jogo", fontWeight = FontWeight.Bold)
        SegmentedControl(
            items = listOf("vsJogador", "vsBot"),
            onItemSelection = {},
            color = MaterialTheme.colorScheme.secondaryContainer,
            contrastColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Text("Nome dos Jogadores", fontWeight = FontWeight.Bold)
        TextField(value = "", onValueChange = {}, label = { Text("Jogador 1") })
        TextField(value = "", onValueChange = {}, label = { Text("Jogador 2") })
        Text("Tamanho do Tabuleiro", fontWeight = FontWeight.Bold)
        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            SegmentedControl(
                items = listOf("3x3", "4x4", "5x5", "6x6", "7x7", "8x8", "9x9", "10x10"),
                onItemSelection = {},
                color = MaterialTheme.colorScheme.secondaryContainer,
                contrastColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        Button(onClick = { /*TODO*/ }) {
            Text("Começar  Partida")
        }
        Button(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text("Histórico de Partidas", color = MaterialTheme.colorScheme.onSecondary)
        }
    }
}