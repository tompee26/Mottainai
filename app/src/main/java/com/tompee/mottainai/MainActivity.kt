package com.tompee.mottainai

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import com.tompee.mottainai.view.adapter.MainPagerAdapter
import com.tompee.mottainai.view.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState, true)
        setContentView(R.layout.activity_main)

        val viewPager = findViewById(R.id.view_pager) as ViewPager
        viewPager.adapter = MainPagerAdapter(supportFragmentManager, this)

        (findViewById(R.id.tab_layout) as TabLayout).setupWithViewPager(viewPager)
    }
}
