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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
fun NormalizedSegmentedControl(options: List<String>, modifier: Modifier = Modifier) {
    SegmentedControl(
        items = options,
        onItemSelection = {},
        color = MaterialTheme.colorScheme.secondaryContainer,
        contrastColor = MaterialTheme.colorScheme.onSecondaryContainer,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(10.dp)
            .fillMaxHeight(),
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
                NormalizedSegmentedControl(
                    options = listOf(
                        stringResource(R.string.game_type_against_player),
                        stringResource(R.string.game_type_against_bot)
                    )
                )
            }
            Spacer(Modifier.height(20.dp))
            FormSection(title = R.string.player_name_input_section_label) {
                TextField(
                    value = "",
                    onValueChange = {},
                    label = { Text(stringResource(R.string.player_1_name_input)) })
                TextField(
                    value = "",
                    onValueChange = {},
                    label = { Text(stringResource(R.string.player_2_name_input)) })
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        FormSection(title = R.string.table_size_selection_label) {
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                NormalizedSegmentedControl(options = IntStream.range(3, 11).mapToObj { "$it x $it" }
                    .collect(Collectors.toList()))
            }
        }
        Spacer(modifier = Modifier.height(100.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    stringResource(R.string.start_game_button),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    stringResource(R.string.game_history_button),
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Preview
@Composable
fun StartScreenPreview() {
    StartScreen(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()
    )
}