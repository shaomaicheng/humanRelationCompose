package com.hangshun.huadian.android.common.utils

import android.content.Context
import android.util.TypedValue

// 方法2
fun dp2px(ctx: Context, dp: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        ctx.resources.displayMetrics
    ).toInt()
}