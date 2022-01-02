package com.hangshun.huadian.android.common.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.hangshun.huadian.android.common.R
import com.hangshun.huadian.android.common.drawable.configDrawable

class PrimaryButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        includeFontPadding = false
        setTextColor(Color.WHITE)
        textSize = 12f
        gravity = Gravity.CENTER

        background = configDrawable(context) {
            gradientColors = intArrayOf(
             resources.getColor(R.color.primary_begin),
             resources.getColor(R.color.primary_end)
            )
            pressGradientColors = intArrayOf(
                resources.getColor(R.color.press_primary_begin),
                resources.getColor(R.color.press_primary_end)
            )
            radius = 8f
            pressColor = Color.GRAY
        }
    }

    fun renderByCustomColor(
        @ColorInt begin:Int,
        @ColorInt end:Int,
        @ColorInt pressBegin:Int,
        @ColorInt pressEnd:Int,
    ) {
        background = configDrawable(context) {
            gradientColors = intArrayOf(
                begin,
                end
            )
            pressGradientColors = intArrayOf(
                pressBegin,
                pressEnd
            )
            radius = 8f
            pressColor = Color.GRAY
        }
    }
}

@BindingAdapter("beginColor", "endColor", "pressBegin", "pressEnd")
fun customColor(btn:PrimaryButton,@ColorInt beginColor:Int,
                @ColorInt endColor:Int,
                @ColorInt pressBegin:Int,
                @ColorInt pressEnd:Int){
    btn.renderByCustomColor(beginColor, endColor, pressBegin, pressEnd)
}