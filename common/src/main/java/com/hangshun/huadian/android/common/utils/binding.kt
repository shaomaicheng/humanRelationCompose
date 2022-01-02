package com.hangshun.huadian.android.common.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("loadUrl")
fun loadUrl(image:ImageView, url:String?){
    url?.let {url->
        Glide.with(image.context)
            .load(url)
            .into(image)
    }
}