package com.tompee.mottainai.controller.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.tompee.mottainai.R
import com.tompee.mottainai.controller.fragment.BrowseFragment
import com.tompee.mottainai.controller.fragment.SampleFragment

class MainPagerAdapter(fragmentManager: FragmentManager, context: Context) :
        FragmentStatePagerAdapter(fragmentManager) {
    private val PAGE_COUNT = 3
    private val mContext = context
    private val mBrowseFragment = BrowseFragment.newInstance()

    override fun getCount(): Int {
        return PAGE_COUNT
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return mBrowseFragment
        }
        return SampleFragment.newInstance()
    }

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> return mContext.resources.getString(R.string.tab_title_browse)
        }
        return "Fragment" + position
    }
}