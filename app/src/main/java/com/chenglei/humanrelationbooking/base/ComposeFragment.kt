package com.chenglei.humanrelationbooking.base

import android.view.View
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.savedstate.ViewTreeSavedStateRegistryOwner
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.compose.home.Nav
import com.hangshun.huadian.android.common.arch.FragmentBase


 class ComposeFragment:FragmentBase() {
    override fun layoutId(): Int {
        return R.layout.fragment_compose

    }

    override fun initViews(root: View) {
        val composeView = root.findViewById<ComposeView>(R.id.composeView)

        requireActivity().window.let {
            ViewTreeLifecycleOwner.set(it.decorView, requireActivity())
            ViewTreeViewModelStoreOwner.set(it.decorView, requireActivity())
            ViewTreeSavedStateRegistryOwner.set(it.decorView, requireActivity())
        }

        arguments?.getString("host")?.let {
            composeView.setContent {
                MaterialTheme() {
                    Nav(start = it)
                }
            }
        }
    }

    override fun dataBinding(): Boolean {
        return false
    }
}