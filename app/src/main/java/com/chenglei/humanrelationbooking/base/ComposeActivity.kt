package com.chenglei.humanrelationbooking.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.savedstate.ViewTreeSavedStateRegistryOwner
import com.chenglei.humanrelationbooking.compose.home.Nav
import com.hangshun.huadian.android.common.arch.ActivityBase
import com.hangshun.huadian.android.common.arch.ToolbarConfig
import kotlinx.coroutines.InternalCoroutinesApi

class ComposeActivity:ActivityBase() {
    private lateinit var composeView: ComposeView
    override fun needToolBar(): Boolean = false

    override fun toolBarConfig(): ToolbarConfig? = null

    override fun realContentView(): View {
        composeView = ComposeView(this)
        return composeView
    }

    override fun initViews() {
        ViewTreeLifecycleOwner.set(window.decorView, this)
        ViewTreeViewModelStoreOwner.set(window.decorView, this)
        ViewTreeSavedStateRegistryOwner.set(window.decorView, this)
        val host = intent.getStringExtra("host")
        val arguments = intent.getBundleExtra("bundle")
        host?.let {
            composeView.setContent {
                MaterialTheme() {
                    Nav(start = it, argument = arguments)
                }
            }
        }
    }

    companion object {
        fun launch(context: Context, host: String, arguments: Bundle? = null) {
            context.startActivity(Intent(context, ComposeActivity::class.java).apply {
                putExtra("host", host)
                putExtra("bundle", arguments)
            })
        }
    }

}