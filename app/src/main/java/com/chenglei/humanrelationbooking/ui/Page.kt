package com.chenglei.humanrelationbooking.ui

import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.West
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.chenglei.humanrelationbooking.statusbarHeightDp
import com.hangshun.huadian.android.common.arch.lightStatusbar

/**
 * @param handleStatusbar 是否处理状态栏
 * @param pageColor 页面背景色
 * @param lightStatusbar 是否是亮色
 * @param onBack 拦截返回事件
 */

data class TopConfig(
    val needToolbar :Boolean = false,
    val title:String = ""
)


class Backer(private val nav: NavController, private val dispatcher: OnBackPressedDispatcher){
    fun back() {
        if (!nav.popBackStack()) {
            dispatcher.onBackPressed()
        }
    }
}

@Composable
fun rememberBacker() : Backer {
    val nav = rememberNavController()
    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current).onBackPressedDispatcher
    return remember {
        Backer(nav, backDispatcher)
    }
}

@Composable
fun Page(
    handleStatusbar: Boolean,
    pageColor: Color,
    lightStatusbar: Boolean = false,
    onBack: () -> Boolean = {true},
    config:TopConfig= TopConfig(false,""),
    composable: @Composable () -> Unit,
) {
    val nav = rememberNavController()
    val backer = rememberBacker()
    BackHandler(onBack = onBack)
    Box(
        Modifier
            .background(pageColor)
            .fillMaxHeight()
            .fillMaxWidth()) {
        if (handleStatusbar) {
            (LocalContext.current as? AppCompatActivity)?.lightStatusbar(lightStatusbar)
            Column {
                Box(
                    modifier = Modifier
                        .background(Color.Transparent)
                        .fillMaxWidth()
                        .height(height = LocalContext.current.statusbarHeightDp())
                ) {
                }
                if (config.needToolbar) {
                    Scaffold(
//                        drawerBackgroundColor= pageColor,
//                        contentColor = pageColor,
                        topBar = {
                            TopAppBar(
                                backgroundColor = Color.White,
                                title = {
                                    Text(text = config.title)
                                },
                                modifier = Modifier.background(Color.White),
                                navigationIcon = {
                                    Icon(Icons.Rounded.West, contentDescription = "",
                                        Modifier
                                            .padding(start = 12.dp)
                                            .clickable {
                                                backer.back()
                                            })
                                }
                            )
                        }
                    ) {
                        composable()
                    }
                } else {
                    composable()
                }
            }
        } else {
            composable()
        }
    }
}