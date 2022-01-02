package com.chenglei.humanrelationbooking

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.savedstate.ViewTreeSavedStateRegistryOwner
import cn.bmob.v3.BmobUser
import com.chenglei.humanrelationbooking.compose.home.Nav
import com.chenglei.humanrelationbooking.home.HomeActivity
import kotlinx.coroutines.InternalCoroutinesApi


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        window.handleStatusbar()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initOwner()


        if (BmobUser.isLogin()) {
            val userBase = BmobUser.getCurrentUser()
            applog("登录中，用户id：${userBase.objectId}")
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        } else {
            applog("未登录")
            // 跳转登录
            findViewById<ComposeView>(R.id.composeView).setContent {
                MaterialTheme() {
                    Nav()
                }
            }
        }

        /*findViewById<ComposeView>(R.id.composeView).setContent {
            MaterialTheme() {
                Nav()
            }
        }*/
    }

    private fun initOwner() {
        ViewTreeLifecycleOwner.set(window.decorView, this)
        ViewTreeViewModelStoreOwner.set(window.decorView, this)
        ViewTreeSavedStateRegistryOwner.set(window.decorView, this)
    }
}

