package com.chenglei.humanrelationbooking.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chenglei.humanrelationbooking.R


@Preview
@Composable
fun CommonButtonPreview() {
    CommonButton(onClick = {}, enabled = true, Modifier
        .width(200.dp)
        .height(44.dp))
}

@Preview
@Composable
fun CommonButtonDisablePreview() {
    CommonButton(onClick = {}, enabled = false, Modifier
        .width(200.dp)
        .height(44.dp))
}

@Composable
fun CommonButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier,
    title:String = ""
) {
    val shape = RoundedCornerShape(
        topStart = 8.dp,
        topEnd = 8.dp,
        bottomStart = 8.dp,
        bottomEnd = 8.dp
    )
    val enableGradient = Brush.horizontalGradient(colors = listOf(
        colorResource(id = R.color.color_FC775B),
        colorResource(id = R.color.color_F64935)
    ))

    val disableGradient = Brush.horizontalGradient(colors = listOf(
        colorResource(id = R.color.color_FC775B_50),
        colorResource(id = R.color.color_F64935_50)
    ))

    Box(
        modifier = modifier
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
            .background(
                brush = if (enabled) enableGradient else disableGradient,
                shape = shape
            )
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = title,
            textAlign = TextAlign.Center,
            style = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
            )
        )
    }
}