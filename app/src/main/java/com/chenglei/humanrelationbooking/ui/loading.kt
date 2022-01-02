package com.chenglei.humanrelationbooking.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chenglei.humanrelationbooking.R


@Composable
fun Loading(loading:Boolean, modifier: Modifier = Modifier) {
    if (loading) {
        CircularProgressIndicator(
            color = colorResource(id = R.color.primary_500),
            modifier = modifier.width(30.dp).height(30.dp)
        )
    } else {
        Empty()
    }
}


@Composable
@Preview
fun LoadingTest() {
    CircularProgressIndicator(color = colorResource(id = R.color.primary_500), modifier = Modifier.size(50.dp)
    )
}

@Composable
fun Empty() {
    Box(
        Modifier
            .height(0.dp)
            .width(0.dp)) {

    }
}