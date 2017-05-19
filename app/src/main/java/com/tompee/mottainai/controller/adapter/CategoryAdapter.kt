package com.tompee.mottainai.controller.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso
import com.tompee.mottainai.R
import com.tompee.mottainai.controller.fragment.BannerFragment
import com.tompee.mottainai.controller.utility.ViewUtility
import com.tompee.mottainai.model.Category
import io.realm.Realm
import java.util.*

class CategoryAdapter(val fragmentManager: FragmentManager) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val bannerCount: Int
    val categoryCount: Int

    init {
        bannerCount = 5
        categoryCount = 10
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        when (viewType) {
            0 -> {
                viewHolder = BannerViewHolder(parent?.context, fragmentManager, LayoutInflater.from(parent?.context).
                        inflate(R.layout.banner_view, parent, false))
            }
            else -> viewHolder = CategoryViewHolder(parent?.context, LayoutInflater.from(parent?.context).
                    inflate(R.layout.category_item, parent, false))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        // TODO update with correct information
        if (holder is CategoryViewHolder) {
            holder.bind("http://c1.staticflickr.com/4/3434/3202687733_4dff526fb0_b.jpg",
                    "category " + position)
        } else if (holder is BannerViewHolder) {
            val urlList = ArrayList<String>()
            urlList.add("http://www.fashionlivre.com/filemanager/source/Shopping%20Guide/Sale/1.jpg")
            urlList.add("http://www.fashionlivre.com/filemanager/source/Shopping%20Guide/Sale/1.jpg")
            urlList.add("http://www.fashionlivre.com/filemanager/source/Shopping%20Guide/Sale/1.jpg")
            holder.bind(urlList)
        }
    }

    override fun getItemCount(): Int {
        return categoryCount
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0 else 1
    }

    class CategoryViewHolder(val context: Context?, itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryImageView = itemView.findViewById(R.id.category_image) as ImageView
        private val categoryTextView = itemView.findViewById(R.id.category_name) as TextView

        fun bind(url: String, categoryName: String) {
            Picasso.with(context).load(url).into(categoryImageView)
            categoryTextView.text = categoryName
        }
    }

    class BannerViewHolder(val context: Context?, val fragmentManager: FragmentManager,
                           view: View) : RecyclerView.ViewHolder(view), ViewPager.OnPageChangeListener {
        private val viewpager: ViewPager = view.findViewById(R.id.banner_view_pager) as ViewPager
        private val indicator: RadioGroup = view.findViewById(R.id.banner_indicator) as RadioGroup

        init {
            viewpager.addOnPageChangeListener(this)
        }

        fun bind(urlList: List<String>) {
            val realm = Realm.getDefaultInstance()
            val query = realm.where(Category::class.java)
            var results = query.findAll()

            viewpager.adapter = object : FragmentStatePagerAdapter(fragmentManager) {
                override fun getItem(position: Int): Fragment {
                    return BannerFragment.newInstance(urlList[position])
                }

                override fun getCount(): Int {
                    return urlList.size
                }
            }

            indicator.orientation = RadioGroup.HORIZONTAL
            for (url: String in urlList) {
                val radioButton = RadioButton(context)
                val layoutParams = LinearLayout.LayoutParams(ViewUtility.convertDpToPixel(12,
                        context!!), ViewGroup.LayoutParams.WRAP_CONTENT)
                layoutParams.gravity = Gravity.CENTER
                layoutParams.leftMargin = ViewUtility.convertDpToPixel(12, context)
                layoutParams.rightMargin = ViewUtility.convertDpToPixel(12, context)
                radioButton.layoutParams = layoutParams
                radioButton.isClickable = false
                radioButton.buttonDrawable = ContextCompat.getDrawable(context,
                        R.drawable.selector_radio_button)
                indicator.addView(radioButton)
            }
            updateIndicator()
        }

        private fun updateIndicator() {
            (indicator.getChildAt(viewpager.currentItem) as RadioButton).isChecked = true
        }

        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            updateIndicator()
        }
    }
}