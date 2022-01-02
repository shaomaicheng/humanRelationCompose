package com.chenglei.humanrelationbooking.compose.mine

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.base.ComposeActivity
import com.chenglei.humanrelationbooking.compose.home.Hosts
import com.chenglei.humanrelationbooking.ui.ButtonNoIndication


@Composable
fun MineHeader() {
    val nickname = remember {
        "cheng"
    }
    val context = LocalContext.current
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(190.dp)
        .background(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    colorResource(id = R.color.color_FC775B),
                    colorResource(id = R.color.color_F64935)
                )
            )
        )
    ) {

        Row (
            Modifier.padding(top = 68.dp, start = 12.dp, end = 12.dp)
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box() {
                    Image(painter = painterResource(id = R.drawable.default_avatar), contentDescription = "",
                        Modifier
                            .size(64.dp)
                            .background(shape = CircleShape, color = Color.Transparent))
                }

                Column(Modifier.padding(start = 12.dp)) {
                    Text(
                        text = if (nickname.isNotEmpty()) nickname else stringResource(id = R.string.setting_nickname),
                        fontSize = if (nickname.isNotEmpty()) 24.sp else 12.sp,
                        fontWeight = if (nickname.isNotEmpty()) FontWeight.W600 else FontWeight.W400,
                        color = if (nickname.isNotEmpty()) Color.White else colorResource(id = R.color.color_ccccccc),
                    )
                    Box(modifier = Modifier.height(19.dp))
                }
            }

            ButtonNoIndication(onClick = {
                ComposeActivity.launch(context, Hosts.Settings)
            }, modifier = Modifier.width(24.dp).height(50.dp)) {

                Image(painter = painterResource(id = R.drawable.arrow_right_white), contentDescription = null,
                Modifier.width(24.dp).height(24.dp))
            }
        }
    }
}