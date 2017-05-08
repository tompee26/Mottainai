package com.tompee.mottainai.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.tompee.mottainai.view.fragment.SampleFragment

class MainPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    private val PAGE_COUNT = 3

    override fun getCount(): Int {
        return PAGE_COUNT
    }

    override fun getItem(position: Int): Fragment {
        return SampleFragment.newInstance()
    }

    override fun getPageTitle(position: Int): CharSequence {
        return "Fragment" + position
    }
}