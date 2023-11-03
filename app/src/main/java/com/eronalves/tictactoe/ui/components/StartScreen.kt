package com.eronalves.tictactoe.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.eronalves.tictactoe.R
import java.util.stream.Collectors
import java.util.stream.IntStream

@Composable
fun FormSection(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    children: @Composable (() -> Unit) = {},
) {
    Text(
        text = stringResource(title),
        fontWeight = FontWeight.Bold,
        fontSize = TextUnit(20.0F, TextUnitType.Sp),
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(10.dp))
    children()
}

@Composable
fun NormalizedSegmentedControl(
    options: List<String>, onItemSelection: (Int) -> Unit, modifier: Modifier = Modifier
) {
    SegmentedControl(
        items = options,
        onItemSelection = onItemSelection,
        color = MaterialTheme.colorScheme.secondaryContainer,
        contrastColor = MaterialTheme.colorScheme.onSecondaryContainer,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(
    onStartGame: (player1Name: String, player2Name: String, isRobotEnabled: Boolean, selectedTableSize: Int) -> Unit,
    onLoadHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gameTypes = listOf(
        stringResource(R.string.game_type_against_player),
        stringResource(R.string.game_type_against_bot)
    )
    val robotPlayerName = stringResource(R.string.robot_player_name)
    var gameTypeIndex by remember { mutableIntStateOf(0) }
    var player1Name by remember { mutableStateOf("") }
    var player2Name by remember { mutableStateOf("") }
    var player2IsEnabled by remember { mutableStateOf(true) }
    var selectedTableSize by remember { mutableStateOf(3) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(
                id = if (isSystemInDarkTheme()) {
                    R.drawable.app_logo_dark
                } else {
                    R.drawable.app_logo_light
                }
            ),
            contentDescription = stringResource(R.string.app_logo_description),
            Modifier.size(200.dp)
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            FormSection(title = R.string.game_type_label) {
                NormalizedSegmentedControl(options = gameTypes,
                    onItemSelection = {
                        gameTypeIndex = it
                        if (it == 1) {
                            player2IsEnabled = false
                            player2Name = robotPlayerName
                            return@NormalizedSegmentedControl
                        }

                        player2IsEnabled = true
                        player2Name = ""

                    })
            }
            Spacer(Modifier.height(20.dp))
            FormSection(title = R.string.player_name_input_section_label) {
                TextField(value = player1Name,
                    onValueChange = { player1Name = it },
                    label = { Text(stringResource(R.string.player_1_name_input)) })
                TextField(value = player2Name,
                    onValueChange = { player2Name = it },
                    enabled = player2IsEnabled,
                    label = { Text(stringResource(R.string.player_2_name_input)) })
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        FormSection(title = R.string.table_size_selection_label) {
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                NormalizedSegmentedControl(options = IntStream
                    .range(3, 11)
                    .mapToObj { "$it x $it" }
                    .collect(Collectors.toList()),
                    onItemSelection = {
                        selectedTableSize = it + 3
                    })
            }
        }
        Spacer(modifier = Modifier.height(100.dp))
        BottomControls(
            primaryButtonLabel = R.string.start_game_button,
            primaryButtonOnClick = {
                onStartGame(
                    player1Name,
                    player2Name,
                    gameTypeIndex == 1,
                    selectedTableSize
                )
            },
            primaryButtonEnabled = !player1Name.isEmpty() && !player2Name.isEmpty(),
            secondaryButtonLabel = R.string.game_history_button,
            secondaryButtonOnClick = onLoadHistory
        )
    }
}

@Preview
@Composable
fun StartScreenPreview() {
    StartScreen(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(),
        onStartGame = { player1Name, player2Name, isRobotEnabled, selectedTableSize -> },
        onLoadHistory = {}
    )
}