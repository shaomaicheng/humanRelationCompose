package com.chenglei.humanrelationbooking.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.chenglei.humanrelationbooking.R


@Composable
fun Line(modifier: Modifier = Modifier){
    Box(
        modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(colorResource(id = R.color.color_f6f6f6)))
}