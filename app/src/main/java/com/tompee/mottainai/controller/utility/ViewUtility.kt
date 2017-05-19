package com.tompee.mottainai.controller.utility

import android.content.Context
import android.util.DisplayMetrics

class ViewUtility {

    companion object {
        fun convertDpToPixel(dp: Int, context: Context): Int {
            val resources = context.resources
            val metrics = resources.displayMetrics
            val px = dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
            return px.toInt()
        }

        fun convertPixelsToDp(px: Int, context: Context): Int {
            val resources = context.resources
            val metrics = resources.displayMetrics
            val dp = px / (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
            return dp.toInt()
        }

    }
}