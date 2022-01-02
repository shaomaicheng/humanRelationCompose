package com.chenglei.humanrelationbooking.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chenglei.humanrelationbooking.R


@Composable
fun Arrow(clicker: ()->Unit, value:String){
    Box(
        Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.White)
            .clickable {
                clicker.invoke()
            }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(Color.White)
        ) {

            Box(Modifier.padding(start = 12.dp)) {
                Text(
                    value,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.color_666666)
                )
            }

            ButtonNoIndication(onClick = {},
                modifier = Modifier.padding(end = 20.dp)) {
                Image(painter = painterResource(id = R.drawable.arrow_right), contentDescription = null, modifier = Modifier
                    .height(18.dp)
                    .width(18.dp))
            }
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .padding(start = 12.dp, end = 12.dp)
            .align(Alignment.BottomCenter)
            .background(colorResource(id = R.color.color_f6f6f6)),)
    }
}