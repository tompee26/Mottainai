package com.tompee.mottainai.view.fragment

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

class SampleFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?,
                              @Nullable savedInstanceState: Bundle?): View? {
        val viewGroup = LinearLayout(context)
        viewGroup.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        val textView = TextView(context)
        textView.text = "Fragment"
        viewGroup.addView(textView)
        return viewGroup
    }

    companion object {
        fun newInstance(): SampleFragment {
            return SampleFragment()
        }
    }
}