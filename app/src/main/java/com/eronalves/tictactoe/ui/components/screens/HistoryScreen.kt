package com.eronalves.tictactoe.ui.components.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.eronalves.tictactoe.R
import com.eronalves.tictactoe.data.Winner
import com.eronalves.tictactoe.ui.components.NormalizedSegmentedControl
import com.eronalves.tictactoe.ui.components.viewmodel.GlobalStateViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@Composable
fun HistoryScreen(
    onClickBack: () -> Unit,
    viewModel: GlobalStateViewModel,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(true) {
        viewModel.getListedRoundPages()
        viewModel.getHistoryPaginatedFrom(0)
    }
    val pageList by viewModel.roundItemCountState.collectAsState()

    Column(modifier = modifier, verticalArrangement = Arrangement.SpaceBetween) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.verticalScroll(
                rememberScrollState()
            )
        ) {
            pageList.roundList?.forEach {
                println(pageList.roundList)
                val victoriousPlayer = when (it.round.winner) {
                    Winner.Player1 -> it.player1?.name
                    Winner.Player2 -> it.player2?.name
                    Winner.Match -> "Velha"
                    else -> "Jogo Inacabado"
                }
                Card(
                    Modifier
                        .padding(2.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "${it.player1?.name} x ${it.player2?.name}")
                    Text(
                        text = "Vitorioso: ${victoriousPlayer}"
                    )
                    Text(
                        text = "Jogo feito em: ${
                            it.round.startedAt?.let { it1 ->
                                SimpleDateFormat("dd / MM / yyyy - HH:mm").format(
                                    it1
                                )
                            }
                        }"
                    )
                }
            }
        }
        Column {
            Row(modifier = Modifier.verticalScroll(rememberScrollState())) {
                pageList.itemCount?.let {
                    NormalizedSegmentedControl(
                        options = it.map { "$it" },
                        onItemSelection = { item ->
                            viewModel.getHistoryPaginatedFrom(item)
                        })
                }
            }
            Button(
                onClick = onClickBack,
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
            ) {
                Text(
                    stringResource(R.string.history_screen_go_back),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}
