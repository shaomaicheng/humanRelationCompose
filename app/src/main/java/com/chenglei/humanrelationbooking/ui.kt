package com.chenglei.humanrelationbooking

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.chenglei.humanrelationbooking.dialog.LoadingDialog


val Context.appDisplayMetrics: DisplayMetrics
    get() = resources.displayMetrics

fun Context.px2dp(px: Int): Int = px2dp(px.toFloat())
fun Context.px2dpF(px: Int): Float = px2dpF(px.toFloat())

fun Context.px2dp(px: Float): Int = (px / appDisplayMetrics.density).toInt()
fun Context.px2dpF(px: Float): Float = px / appDisplayMetrics.density

fun Context.spToPx(sp: Int): Int = (sp * appDisplayMetrics.scaledDensity + 0.5f).toInt()

fun Context.launchDialog(
    clazz: Class<out DialogFragment>, bundle: Bundle? = null,
    launchNow: Boolean = true
): DialogFragment? {
    return (this as? AppCompatActivity)?.let { activity ->
        val dialog = clazz.newInstance()
        dialog.arguments = bundle
        if (launchNow) {
            dialog.show(activity.supportFragmentManager, clazz.canonicalName)
        }
        dialog
    }
}

fun Context.hideKeyboard(){
    (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.let {
        (this as? Activity).let { activity ->
            it.hideSoftInputFromWindow(activity?.window?.decorView?.windowToken, 0)
        }
    }
}