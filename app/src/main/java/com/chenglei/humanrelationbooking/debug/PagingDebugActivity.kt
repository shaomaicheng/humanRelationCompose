package com.chenglei.humanrelationbooking.debug

import android.view.LayoutInflater
import android.view.View
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.databinding.ActivityDebugPagingBinding
import com.hangshun.huadian.android.common.arch.ActivityBase
import com.hangshun.huadian.android.common.arch.ToolbarConfig
import com.hangshun.huadian.android.common.arch.toolbarConfig

class PagingDebugActivity:ActivityBase() {
    override fun needToolBar(): Boolean {
        return true
    }

    override fun toolBarConfig(): ToolbarConfig? {
        return toolbarConfig {
            color = R.color.white
            navigatorColor=R.color.black
            textColor = R.color.black
            title = getString(R.string.debug_paging_title)
        }
    }

    override fun realContentView(): View {
        return ActivityDebugPagingBinding.inflate(layoutInflater).root
    }

    override fun initViews() {
    }
}