package com.hangshun.huadian.android.common.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.hangshun.huadian.android.common.R
import com.hangshun.huadian.android.common.drawable.configDrawable

class NormalItem @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val tvTitle by lazy {
        findViewById<TextView>(R.id.tvTitle)
    }

    private val tvContent by lazy {
        findViewById<TextView>(R.id.tvContent)
    }

    private val imgRight by lazy {
        findViewById<ImageView>(R.id.imgRight)
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_normal_item, this, true)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.NormalItem)
        val title = typedArray.getString(R.styleable.NormalItem_title)
        val content = typedArray.getString(R.styleable.NormalItem_itemContent)
        val iconRes =
            typedArray.getResourceId(R.styleable.NormalItem_arrowIcon, R.drawable.arrow_right)
        val titleColor =
            typedArray.getColor(R.styleable.NormalItem_titleColor, Color.parseColor("#595959"))
        val contentColor =
            typedArray.getColor(R.styleable.NormalItem_contentColor, Color.parseColor("#8c8c8c"))
        typedArray.recycle()

        this.background = configDrawable(context) {
            solid = Color.WHITE
        }

        tvTitle.text = title
        tvContent.text = content
        imgRight.setImageResource(iconRes)
        tvTitle.setTextColor(titleColor)
        tvContent.setTextColor(contentColor)
    }

    fun setTitle(title: String) {
        tvTitle.text = title
    }

    fun setTitleColor(color: Int) {
        tvTitle.setTextColor(color)
    }

    fun setArrowIcon(iconRes: Drawable) {
        imgRight.setImageDrawable(iconRes)
    }

    fun setContent(content: String) {
        tvContent.text = content
    }
}