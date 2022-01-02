package com.chenglei.humanrelationbooking

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Window.handleStatusbar() {
    clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    statusBarColor = Color.TRANSPARENT
}

fun Context.statusbarHeight() : Int {
    val resourceId = applicationContext.resources.getIdentifier("status_bar_height", "dimen", "android")
    return resourceId.takeIf { it > 0 }?.let {
        applicationContext.resources.getDimensionPixelSize(resourceId)
    } ?: 0
}

fun Context.statusbarHeightDp() : Dp {
    return applicationContext.px2dp(statusbarHeight()).dp
}


fun Context.bottomBarHeight():Int {
    val resourceId = applicationContext.resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return resourceId.takeIf { it > 0 }?.let {
        applicationContext.resources.getDimensionPixelSize(resourceId)
    } ?: 0
}

fun Context.bottomBarHeightDp():Dp{
    return applicationContext.px2dp(bottomBarHeight()).dp
}