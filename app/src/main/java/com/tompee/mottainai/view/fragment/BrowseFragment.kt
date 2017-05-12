package com.tompee.mottainai.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tompee.mottainai.R
import com.tompee.mottainai.view.adapter.CategoryAdapter

class BrowseFragment : Fragment() {
    companion object {
        fun newInstance(): BrowseFragment {
            return BrowseFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_browse, container, false)
        val recyclerView = view?.findViewById(R.id.browse_recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = CategoryAdapter(context, fragmentManager)
        return view
    }
}