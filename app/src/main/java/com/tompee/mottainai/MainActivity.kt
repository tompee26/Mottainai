package com.tompee.mottainai

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.View
import com.tompee.mottainai.controller.adapter.MainPagerAdapter
import com.tompee.mottainai.controller.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState, true)

        /** Check if already logged in */
        if (true) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
        setContentView(R.layout.activity_main)

        val viewPager = findViewById(R.id.view_pager) as ViewPager
        viewPager.adapter = MainPagerAdapter(supportFragmentManager, this)
        viewPager.overScrollMode = View.OVER_SCROLL_NEVER

        (findViewById(R.id.tab_layout) as TabLayout).setupWithViewPager(viewPager)
    }
}
