package com.chenglei.humanrelationbooking.compose.mine

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.ui.Line
import com.chenglei.humanrelationbooking.utils.toast

@Composable
fun MineItems() {
    val bgColor = colorResource(id = R.color.color_f6f6f6)
    val blockMo = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(
            color = Color.Red, shape = RoundedCornerShape(8.dp)
        )


    val scrollState = rememberScrollState()

    Box(modifier = Modifier
        .padding(top = 12.dp, start = 12.dp, end = 12.dp, bottom = 4.dp)
        .fillMaxSize()
        .background(bgColor)
    ) {
        Column(
            Modifier.verticalScroll(scrollState)
        ) {
            Box() {
                Column(modifier = blockMo) {
                    MineArrowItem(R.drawable.icon_books_manager,
                        R.string.mine_books_manager,
                        false,
                        true,
                    ) {
                        toast(R.string.next_version_will_have)
                    }
                }
            }

            Box(modifier = Modifier.padding(top = 12.dp)) {
                Column(modifier = blockMo) {
                    MineArrowItem(R.drawable.icon_share,
                        R.string.mine_share,
                        top = true,
                    ) {
                        toast(R.string.next_version_will_have)
                    }
                    MineArrowItem(R.drawable.icon_fallback,
                        R.string.mine_fallback,
                        false
                    ) {
                        toast(R.string.next_version_will_have)
                    }
                    MineArrowItem(R.drawable.icon_contact_us,
                        R.string.mine_contact_us,
                        bottom = true,
                    ) {
                        toast(R.string.next_version_will_have)
                    }
                }
            }

            Box(modifier = Modifier.padding(top = 12.dp)) {
                Column(modifier = blockMo) {
                    MineArrowItem(R.drawable.icon_about,
                        R.string.mine_about,
                        top = true,
                    ) {
                        toast(R.string.next_version_will_have)
                    }
                    MineArrowItem(R.drawable.icon_answer,
                        R.string.mine_answer,
                        false
                    ) {
                        toast(R.string.next_version_will_have)
                    }
                }
            }
        }
    }
}


@Composable
fun MineArrowItem(
    iconRes:Int,
    titleRes:Int,
    top:Boolean=false,
    bottom:Boolean=false,
    click:()->Unit,
) {
    fun getShape(): Shape {
        return if (top) {
            RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
        } else if (bottom) {
            RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
        } else if (top && bottom) {
            RoundedCornerShape(8.dp)
        } else {
            RoundedCornerShape(0.dp)
        }
    }
    Box(
        Modifier
            .height(56.dp)
            .fillMaxWidth()
            .clickable {
                click.invoke()
            }
            .background(Color.White, shape = getShape()),
    ) {
        Row(
            Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,) {
                Image(painter = painterResource(id = iconRes), contentDescription = null,
                    Modifier.padding(start = 12.dp,end = 12.dp))
                Text(text = stringResource(id = titleRes), fontSize = 16.sp,    color = colorResource(id = R.color.color_333333))
            }

            Image(painter = painterResource(id = R.drawable.arrow_right), contentDescription = null,
                Modifier.padding(end = 12.dp))
        }
        if (!bottom) {
            Line(
                Modifier
                    .padding(start = 12.dp, end = 12.dp)
                    .align(Alignment.BottomCenter))
        }
    }
}