package com.hangshun.huadian.android.common.arch

import android.app.Activity
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.hangshun.huadian.android.common.R
import com.hangshun.huadian.android.common.utils.dp2px

abstract class ActivityBase : AppCompatActivity() {

    private var root: ConstraintLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        root = ConstraintLayout(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = resources.getColor(R.color.primary)
        }
        setContentView(
            root,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        val toolbarConfig = toolBarConfig()
        handleStatusBar()
        transparentStatusbar(toolbarConfig?.transparentStatusBar ?:true)
        lightStatusbar(toolbarConfig?.lightStatusBar ?: true)
        if (needToolBar()) {
            val toolbar = Toolbar(this).apply {
                id = R.id.toolbar
                setTheme(R.style.ThemeOverlay_AppCompat_ActionBar)
                popupTheme = R.style.ThemeOverlay_AppCompat_Light
                setTitleTextColor(toolbarConfig?.textColor ?: Color.WHITE)
                setBackgroundColor(resources.getColor(toolbarConfig?.color ?: R.color.primary))
                title = toolbarConfig?.title ?: ""
                setSubtitleTextColor(toolbarConfig?.textColor ?: Color.WHITE)

            }


            root?.addView(toolbar, ConstraintLayout.LayoutParams(
                0, dp2px(this, toolbarConfig?.height ?: 80f)
            ).apply {
                startToStart = 0
                endToEnd = 0
                topToTop = 0
                topMargin = resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))
            })
            setSupportActionBar(toolbar)
            val showArrow = toolbarConfig?.showNavigation ?: true
            supportActionBar?.setDisplayHomeAsUpEnabled(showArrow)
            supportActionBar?.setHomeButtonEnabled(showArrow)
            if (showArrow) {
                toolbar.navigationIcon?.let { drawable ->
                    drawable.setColorFilter(toolbarIconColor(toolbarConfig), PorterDuff.Mode.SRC_ATOP)
                    supportActionBar?.setHomeAsUpIndicator(drawable)
                }
            }
            toolbar.setNavigationOnClickListener(
                toolbarConfig?.navigatorClicker ?: View.OnClickListener { this.finish() })
        }

        val realContent = realContentView()
        root?.addView(realContent, ConstraintLayout.LayoutParams(
            0, 0
        ).apply {
            if (needToolBar()) {
                startToStart = 0
                endToEnd = 0
                topToBottom = R.id.toolbar
                bottomToBottom = 0
            } else {
                startToStart = 0
                endToEnd = 0
                topToTop = 0
                bottomToBottom = 0
            }
        })

        initViews()
    }

    abstract fun needToolBar(): Boolean
    abstract fun toolBarConfig(): ToolbarConfig?
    abstract fun realContentView(): View
    abstract fun initViews()

    fun getRoot(): ViewGroup? = root

    private fun toolbarIconColor(toolbarConfig: ToolbarConfig?):Int {
        return toolbarConfig?.navigatorColor ?: Color.WHITE
    }
}


class ToolbarConfig {
    var height: Float = 56f
    @ColorRes
    var color: Int = R.color.primary
    @ColorInt
    var textColor: Int = Color.WHITE
    var title: String = ""
    var showNavigation: Boolean = true
    var navigatorClicker: View.OnClickListener? = null
    var lightStatusBar: Boolean = true
    var transparentStatusBar: Boolean = true
    @ColorInt
    var navigatorColor : Int = Color.WHITE
}

fun Activity.lightStatusbar(light: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val decorView = window.decorView
        if (light) {

            decorView.systemUiVisibility = decorView.systemUiVisibility or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decorView.systemUiVisibility = decorView.systemUiVisibility and
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
    }
}

fun Activity.transparentStatusbar(enable: Boolean) {
    if (enable) {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    } else {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    }
}

fun Activity.handleStatusBar(){
    window.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = Color.TRANSPARENT
    }
}

fun ActivityBase.toolbarConfig(init: ToolbarConfig.() -> Unit): ToolbarConfig {
    val config = ToolbarConfig()
    config.init()
    return config
}