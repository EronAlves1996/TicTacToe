package com.eronalves.tictactoe.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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