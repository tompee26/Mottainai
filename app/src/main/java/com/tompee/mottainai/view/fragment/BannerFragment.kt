package com.tompee.mottainai.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso

class BannerFragment : Fragment() {

    companion object {
        const val URL_KEY = "url"

        fun newInstance(url: String): BannerFragment {
            val bannerFragment = BannerFragment()
            val bundle = Bundle()
            bundle.putString(URL_KEY, url)
            bannerFragment.arguments = bundle
            return bannerFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val imageView = ImageView(context)
        imageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        val url = arguments.getString(URL_KEY)!!
        Picasso.with(context).load(url).into(imageView)
        return imageView
    }
}