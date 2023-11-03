package com.eronalves.tictactoe.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun BottomControls(
    @StringRes primaryButtonLabel: Int,
    primaryButtonOnClick: () -> Unit,
    primaryButtonEnabled: Boolean,
    @StringRes secondaryButtonLabel: Int,
    secondaryButtonOnClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Button(
            onClick = primaryButtonOnClick,
            modifier = Modifier
                .padding(bottom = 5.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            enabled = primaryButtonEnabled
        ) {
            Text(
                stringResource(primaryButtonLabel),
                style = MaterialTheme.typography.titleLarge
            )
        }
        Button(
            onClick = secondaryButtonOnClick,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                stringResource(secondaryButtonLabel),
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}