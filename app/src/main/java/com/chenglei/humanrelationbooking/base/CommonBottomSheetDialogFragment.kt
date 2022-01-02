package com.chenglei.humanrelationbooking.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import com.chenglei.humanrelationbooking.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hangshun.huadian.android.common.drawable.configDrawable
import com.hangshun.huadian.android.common.utils.dp2px

abstract class CommonBottomSheetDialogFragment: BottomSheetDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.TransparentBottomSheetStyle)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragmentRoot = layoutInflater.inflate(R.layout.fragment_common_bottomsheet,container,false)
        dialog?.setContentView(ConstraintLayout(requireContext()).apply { background = ColorDrawable(Color.TRANSPARENT) })
       dialog?.window?.findViewById<FrameLayout>(R.id.design_bottom_sheet)?.background = ColorDrawable(Color.TRANSPARENT)
        val config = bottomSheetConfig()?: defaultConfig()
        fragmentRoot.background = config.background
        fragmentRoot.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
                    dp2px(requireContext(), config.height)
        )
        (dialog as? BottomSheetDialog)?.let { dialog->
            dialog.behavior.peekHeight = dp2px(requireContext(),config.peekHeight)
        }
        if (fragmentRoot is ConstraintLayout) {
            val binding = DataBindingUtil.inflate<ViewDataBinding>(
                layoutInflater,
                layoutId(),
                fragmentRoot,
                true
            )
            initViews(binding = binding)
        }
        return fragmentRoot
    }

    fun defaultConfig():BottomSheetDialogConfig {
        val background = configDrawable(requireContext()) {
            solid = ContextCompat.getColor(requireContext(),R.color.white)
            leftTopRadius = 10f
            rightTopRadius = 10f
        }
        return BottomSheetDialogConfig(
            704f, background, 704f
        )
    }

    @LayoutRes abstract fun layoutId():Int
    abstract fun bottomSheetConfig():BottomSheetDialogConfig?
    abstract fun initViews(binding:ViewDataBinding)
}

data class BottomSheetDialogConfig(
    var height:Float,
    var background:Drawable,
    var peekHeight:Float
)

fun <T:CommonBottomSheetDialogFragment> FragmentActivity.launchBottomSheet(clazz:Class<T>, bundle:Bundle? = null) {
    val fragment = (clazz.newInstance() as CommonBottomSheetDialogFragment).apply {
        arguments = bundle
    }
    this.supportFragmentManager.beginTransaction()
        .add(fragment, fragment.hashCode().toString())
        .commit()
}