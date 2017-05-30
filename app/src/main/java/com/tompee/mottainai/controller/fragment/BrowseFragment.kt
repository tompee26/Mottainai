package com.tompee.mottainai.controller.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tompee.mottainai.R
import com.tompee.mottainai.controller.adapter.CategoryAdapter
import kotlinx.android.synthetic.main.fragment_browse.*

class BrowseFragment : Fragment() {
    companion object {
        fun newInstance(): BrowseFragment {
            return BrowseFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_browse, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = CategoryAdapter(fragmentManager)
    }
}