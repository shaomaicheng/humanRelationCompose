package com.chenglei.humanrelationbooking.books

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Size
import android.view.View
import androidx.core.content.ContextCompat
import com.chenglei.humanrelationbooking.R

class ArcLeftView:View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val paint = Paint()

    private var color: Int? = null
    private var size: Size? = null

    fun render(size: Size, color: Int) {
        this.size = size
        this.color = color
        invalidate()
    }


    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        color?.let { color->
            size?.let { size->
                paint.color = ContextCompat.getColor(context, R.color.white_20)
                paint.style = Paint.Style.FILL
                canvas?.drawArc(
                    RectF(-size.width.toFloat(), 0f, size.width.toFloat(), 2*size.height.toFloat()),
                    0f, -90f,true, paint
                )
            }
        }
    }
}
