package com.tompee.mottainai.controller.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.TextView
import com.tompee.mottainai.R

open class BaseActivity : AppCompatActivity() {

    protected fun onCreate(savedInstanceState: Bundle?, withToolbar: Boolean) {
        super.onCreate(savedInstanceState)
        if (withToolbar) {
            val toolbar = findViewById(R.id.toolbar) as? Toolbar
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }
}