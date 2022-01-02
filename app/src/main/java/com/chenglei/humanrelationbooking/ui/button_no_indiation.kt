package com.chenglei.humanrelationbooking.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role


@Composable
fun ButtonNoIndication(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enable:Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable ()->Unit
) {
    Box(
    modifier=modifier.clickable(
        enabled = enable,
        onClick = onClick,
        role = Role.Button,
        interactionSource = interactionSource,
        indication = null
    )
    ) {
        content()
    }
}