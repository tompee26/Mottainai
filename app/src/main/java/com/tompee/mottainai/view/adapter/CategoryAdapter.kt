package com.tompee.mottainai.view.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.tompee.mottainai.R
import com.tompee.mottainai.view.fragment.BannerFragment

class CategoryAdapter(val context: Context, val fragmentManager: FragmentManager) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val bannerCount : Int

    init {
        // TODO
        bannerCount = 5
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        when (viewType) {
            0 -> {
                viewHolder = BannerViewHolder(LayoutInflater.from(context).inflate(R.layout.
                        banner_view, parent, false))
            }
            else -> viewHolder = CategoryViewHolder(LayoutInflater.from(context).
                    inflate(R.layout.category_item, parent, false))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is CategoryViewHolder) {
            // TODO
            Picasso.with(context).load("http://c1.staticflickr.com/4/3434/3202687733_4dff526fb0_b.jpg").into(holder.categoryImageView)
            holder.categoryTextView.text = "Category " + position
        } else if (holder is BannerViewHolder) {
            holder.viewpager.adapter = object : FragmentStatePagerAdapter(fragmentManager) {
                override fun getItem(position: Int): Fragment {
                    // TODO
                    return BannerFragment.newInstance("http://www.fashionlivre.com/filemanager/source/Shopping%20Guide/Sale/1.jpg")
                }

                override fun getCount(): Int {
                    return bannerCount
                }
            }
        }
    }

    override fun getItemCount(): Int {
        // TODO
        return 10
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0 else 1
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var categoryImageView: ImageView = itemView.findViewById(R.id.category_image) as ImageView
        var categoryTextView: TextView = itemView.findViewById(R.id.category_name) as TextView
    }

    class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val viewpager: ViewPager
        val indicator: RadioGroup

        init {
            viewpager = view.findViewById(R.id.view_pager) as ViewPager
            indicator = view.findViewById(R.id.banner_indicator) as RadioGroup
        }
    }
}