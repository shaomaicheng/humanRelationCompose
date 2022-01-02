package com.chenglei.humanrelationbooking

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.savedstate.ViewTreeSavedStateRegistryOwner
import com.chenglei.humanrelationbooking.home.HomeActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        window.handleStatusbar()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initOwner()


        startActivity(Intent(this, HomeActivity::class.java))
    }

    private fun initOwner() {
        ViewTreeLifecycleOwner.set(window.decorView, this)
        ViewTreeViewModelStoreOwner.set(window.decorView, this)
        ViewTreeSavedStateRegistryOwner.set(window.decorView, this)
    }
}

