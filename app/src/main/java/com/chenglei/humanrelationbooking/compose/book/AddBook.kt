package com.chenglei.humanrelationbooking.compose.book

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.ui.Page
import com.chenglei.humanrelationbooking.ui.TopConfig

@Composable
fun AddBookPage(navController: NavController) {
    Page(handleStatusbar = true, pageColor = Color.White,
    config = TopConfig(true, stringResource(id = R.string.index_add_book))
    ) {

    }
}