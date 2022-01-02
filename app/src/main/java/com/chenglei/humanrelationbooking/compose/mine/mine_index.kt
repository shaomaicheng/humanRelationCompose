package com.chenglei.humanrelationbooking.compose.mine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.ui.Page
import com.chenglei.humanrelationbooking.ui.TopConfig
import kotlinx.coroutines.InternalCoroutinesApi

@Composable
fun MineIndex() {
    Page(
        handleStatusbar = false, pageColor = Color.White, config = TopConfig(false),
        lightStatusbar = true
    ) {

        Box(
            Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.color_f6f6f6))
        ) {

            MineHeader()


            Column {
                MinePeek()
                MineItems()
            }
        }
    }
}