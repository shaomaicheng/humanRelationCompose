package com.chenglei.humanrelationbooking.compose.settings

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cn.bmob.v3.BmobUser
import com.chenglei.humanrelationbooking.MainActivity
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.ui.Arrow
import com.chenglei.humanrelationbooking.ui.Line
import com.chenglei.humanrelationbooking.ui.Page
import com.chenglei.humanrelationbooking.ui.TopConfig
import com.chenglei.humanrelationbooking.utils.toast
import com.chenglei.humanrelationbooking.version


@Composable
fun Settings(controller: NavController) {
    val context = LocalContext.current
    val logoutConfirmShow = remember {
        mutableStateOf(false)
    }
    Page(
        handleStatusbar = true, pageColor = Color.White,
        lightStatusbar = true,
        config = TopConfig(true, stringResource(id = R.string.settings))
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.color_f6f6f6))
                .padding(top = 8.dp)
        ) {

            Column(
                Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.color_f6f6f6))
            ) {

                Arrow(clicker = {
                    toast(R.string.next_version_will_have)
                }, value = stringResource(id = R.string.settings_profile))

                // 版本
                Arrow(
                    clicker = {
                        // todo 更新
                    },
                    value = String.format(
                        stringResource(id = R.string.settings_version),
                        context.version()
                    )
                )

                //退出
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color.White)
                    .clickable {
                        logoutConfirmShow.value = true
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.settings_logout),
                        Modifier
                            .padding(start = 12.dp)
                            .align(Alignment.CenterStart),
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.color_f14848)
                    )
                    Line(Modifier.align(Alignment.BottomCenter))
                }
            }
        }


        if (logoutConfirmShow.value) {
            AlertDialog(onDismissRequest = {
                logoutConfirmShow.value = false
            }, text = {
                Text(text = stringResource(id = R.string.logout_text))
            }, shape = RoundedCornerShape(8.dp), confirmButton = {
                Text(text = stringResource(id = R.string.confirm),
                    Modifier
                        .clickable {
                            logoutConfirmShow.value = false
                            // 确认注销
                            BmobUser.logOut()
                            context.startActivity(Intent(context, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                            })
                        }
                        .padding(end = 12.dp, start = 20.dp,bottom = 8.dp),
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.color_f14848))
            }, dismissButton = {
                Text(
                    text = stringResource(id = R.string.cancel),
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .clickable {
                            logoutConfirmShow.value = false
                        },
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.color_262626)
                )
            })
        }
    }
}