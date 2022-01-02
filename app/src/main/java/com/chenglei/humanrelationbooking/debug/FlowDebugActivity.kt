package com.chenglei.humanrelationbooking.debug

import android.view.View
import androidx.lifecycle.lifecycleScope
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.databinding.ActivityFlowDebugBinding
import com.chenglei.humanrelationbooking.requireViewModel
import com.hangshun.huadian.android.common.arch.ActivityBase
import com.hangshun.huadian.android.common.arch.BaseViewModel
import com.hangshun.huadian.android.common.arch.ToolbarConfig
import com.hangshun.huadian.android.common.arch.toolbarConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

class FlowDebugActivity : ActivityBase() {

    private lateinit var binding: ActivityFlowDebugBinding

    private val vm by lazy {
        requireViewModel<DebugViewModel>()
    }

    override fun needToolBar(): Boolean {
        return true
    }

    override fun toolBarConfig(): ToolbarConfig {
        return toolbarConfig {
            color = R.color.white
            navigatorColor = R.color.black
            textColor = R.color.black
        }
    }

    override fun realContentView(): View {
        binding = ActivityFlowDebugBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.debugVm = vm
        return binding.root
    }

    override fun initViews() {

        lifecycleScope.launchWhenCreated {

            vm.testValue.collect {
                binding.value.text = it.toString()

            }

        }

        lifecycleScope.launchWhenCreated {
            vm.testValuePlusOne.collect {
                binding.valuePlusOne.text = it.toString()
            }

        }


        binding.btnEmit.setOnClickListener {
            vm.testValue.value = vm.testValue.value + 1
        }

    }
}

class DebugViewModel : BaseViewModel() {
    val testValue = MutableStateFlow(0)

    val testValuePlusOne = testValue.map {
            it + 1
        }
//        .stateIn(viewModelScope,Lazily,0)
}