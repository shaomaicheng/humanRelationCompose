package com.chenglei.humanrelationbooking.h5

import android.content.Context
import android.content.Intent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.widget.Toolbar
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.databinding.ActivityWebviewBinding
import com.hangshun.huadian.android.common.arch.ActivityBase
import com.hangshun.huadian.android.common.arch.ToolbarConfig

class WebViewActivity : ActivityBase() {
    companion object {
        const val URL = "url"
        const val Title = "title"

        fun launch(context: Context, url:String) {
            context.startActivity(Intent(context, WebViewActivity::class.java).apply {
                putExtra(URL, url)
            })
        }
    }

    lateinit var binding: ActivityWebviewBinding

    override fun needToolBar(): Boolean {
        return true
    }

    override fun toolBarConfig(): ToolbarConfig? {
        return ToolbarConfig().apply {
            title = intent.getStringExtra(Title) ?: ""
            color = R.color.white
            navigatorColor = R.color.black
            textColor = R.color.black
        }
    }

    override fun realContentView(): View {
        binding = ActivityWebviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initViews() {
        binding.webview.settings.apply {
            this.javaScriptEnabled = true
        }
       binding.webview.webChromeClient = object : WebChromeClient() {
           override fun onReceivedTitle(view: WebView?, title: String?) {
               super.onReceivedTitle(view, title)
               findViewById<Toolbar>(R.id.toolbar).title = title
           }
       }
        intent.getStringExtra(URL)?.let { url->
            binding.webview.loadUrl(url)
        }
    }

    override fun onBackPressed() {
        if (binding.webview.canGoBack()) {
            binding.webview.goBack()
        } else {
            super.onBackPressed()
        }
    }
}


class H5Path {
    companion object {
        const val Login_Proto = "https://docs.qq.com/doc/DRFdVQmRDc055SlZZ"
    }
}