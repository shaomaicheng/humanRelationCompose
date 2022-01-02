package com.hangshun.huadian.android.common.drawable
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import androidx.annotation.ColorInt
import com.hangshun.huadian.android.common.R
import com.hangshun.huadian.android.common.utils.dp2px


class DrawableConfig{
    @ColorInt var solid:Int? = null
    var stroke:Float? = null
    @ColorInt var  strokeColor: Int? = null
    var radius:Float? = null
    @ColorInt var gradientColors:IntArray?=null

    @ColorInt var pressColor: Int? = null
    @ColorInt var pressGradientColors :IntArray?=null

    var leftTopRadius:Float?=null
    var leftBottomRadius:Float?=null
    var rightTopRadius:Float?=null
    var rightBototmRadius:Float?=null
}

fun configDrawable(context:Context, configProcess: DrawableConfig.()->Unit) : Drawable{
    val config = DrawableConfig()
    configProcess.invoke(config)
    val normal = normalDrawable(context, config)
    return StateListDrawable().apply {
        val press = config.pressColor?.let {
            (normalDrawable(context, config) as GradientDrawable).apply {
                setColor(it)
                config.pressGradientColors?.let { pressColors->
                    colors = pressColors
                }
            }
        }
        press?.let {  addState(intArrayOf(android.R.attr.state_pressed),press)  }
        val disable = (normalDrawable(context, config) as GradientDrawable).apply { alpha = 0x80 }
        addState(intArrayOf(-android.R.attr.state_enabled), disable)
        addState(intArrayOf(),normal)
    }
}

private fun normalDrawable(context: Context, config: DrawableConfig):Drawable{
    return GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        config.radius?.let {
            cornerRadius = dp2px(context, it).toFloat()
        } ?: run {
            cornerRadii = floatArrayOf(
                config.leftTopRadius?.let { dp2px(context,it).toFloat() } ?: 0f,
                config.leftTopRadius?.let { dp2px(context,it).toFloat() } ?: 0f,
                config.rightTopRadius?.let { dp2px(context,it).toFloat() } ?: 0f,
                config.rightTopRadius?.let { dp2px(context,it).toFloat() } ?: 0f,
                config.rightBototmRadius?.let { dp2px(context,it).toFloat() } ?: 0f,
                config.rightBototmRadius?.let { dp2px(context,it).toFloat() } ?: 0f,
                config.leftBottomRadius?.let { dp2px(context,it).toFloat() } ?: 0f,
                config.leftBottomRadius?.let { dp2px(context,it).toFloat() } ?: 0f
            )
        }
        config.solid?.let {
            setColor(it)
        }
        config.stroke?.let {
            setStroke(dp2px(context, it), config.strokeColor?: context.resources.getColor(R.color.transparent))
        }
        config.gradientColors?.let {
            colors = it
        }
    }
}