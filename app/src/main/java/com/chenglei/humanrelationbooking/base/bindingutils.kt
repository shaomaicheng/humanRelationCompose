package com.chenglei.humanrelationbooking.base

import android.view.View
import androidx.databinding.BindingAdapter
import com.hangshun.huadian.android.common.drawable.configDrawable
import com.hangshun.huadian.android.common.widget.PrimaryButton

@BindingAdapter(value = ["solid", "radius", "stroke", "strokeColor"])
fun customBackground(tv:View, customSolid:Int, customRadius:Float, customStroke:Float, customStrokeColor:Int){
    tv.background = configDrawable(tv.context) {
        solid = customSolid
        radius = customRadius
        stroke = customStroke
        strokeColor = customStrokeColor
    }
}

@BindingAdapter(value = ["solid", "radius"])
fun customBackground(tv:View, customSolid:Int, customRadius:Float){
    tv.background = configDrawable(tv.context) {
        solid = customSolid
        radius = customRadius
    }
}

@BindingAdapter("android:enabled")
fun enable(button: PrimaryButton, enable: Boolean) {
    button.isEnabled = enable
}