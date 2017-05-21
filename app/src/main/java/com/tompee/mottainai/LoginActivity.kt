package com.tompee.mottainai

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ImageView
import com.tompee.mottainai.controller.base.BaseActivity
import com.tompee.mottainai.controller.fragment.LoginFragment
import android.R.attr.data




class LoginActivity : BaseActivity(), ViewPager.PageTransformer, LoginFragment.LoginFragmentListener {
    private val FRAGMENT_COUNT = 2
    private var viewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val scrollView = findViewById(R.id.hsv_login) as HorizontalScrollView
        val imageView = findViewById(R.id.iv_login_bg) as ImageView
        viewPager = findViewById(R.id.vp_login) as ViewPager
        viewPager?.overScrollMode = View.OVER_SCROLL_NEVER

        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                val x = ((viewPager!!.width * position + positionOffsetPixels) * computeFactor())
                scrollView.scrollTo(x.toInt(), 0)
            }

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

            private fun computeFactor(): Float {
                return (imageView.width / 2 - viewPager!!.width) / (viewPager!!.width *
                        (viewPager!!.adapter.count - 1)).toFloat()
            }
        })
        viewPager?.setPageTransformer(false, this)
        viewPager?.adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
            private val loginFragment = LoginFragment.newInstance(LoginFragment.LOGIN)
            private val signUpFragment = LoginFragment.newInstance(LoginFragment.SIGN_UP)

            override fun getItem(position: Int): Fragment {
                when (position) {
                    0 -> return loginFragment
                    else -> return signUpFragment
                }
            }

            override fun getCount(): Int {
                return FRAGMENT_COUNT
            }
        }
    }

    override fun transformPage(view: View, position: Float) {
        val pageWidth = view.width

        if (position < -1) {
            view.alpha = 0f
        } else if (position <= 1) {
            val appName = view.findViewById(R.id.tv_app_name)
            appName.translationX = -(pageWidth * position)
            val subtitle = view.findViewById(R.id.tv_app_subtitle)
            subtitle.translationX = -(pageWidth * position)

            val email = view.findViewById(R.id.et_user)
            email.translationX = pageWidth * position
            val emailLabel = view.findViewById(R.id.tv_user_label)
            emailLabel.translationX = pageWidth * position
            val userUnderline = view.findViewById(R.id.view_user_underline)
            userUnderline.translationX = pageWidth * position
            val userIcon = view.findViewById(R.id.iv_user_icon)
            userIcon.translationX = pageWidth * position

            val pass = view.findViewById(R.id.et_pass)
            pass.translationX = pageWidth * position
            val passLabel = view.findViewById(R.id.tv_pass_label)
            passLabel.translationX = pageWidth * position
            val passUnderline = view.findViewById(R.id.view_pass_underline)
            passUnderline.translationX = pageWidth * position
            val passIcon = view.findViewById(R.id.iv_pass_icon)
            passIcon.translationX = pageWidth * position

            val button = view.findViewById(R.id.button_command)
            button.translationX = -(pageWidth * position)

        } else {
            view.alpha = 0f
        }
    }

    override fun onSwitchPage(type: Int) {
        if (type == LoginFragment.LOGIN) {
            viewPager?.currentItem = 1
        } else {
            viewPager?.currentItem = 0
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment = (viewPager?.adapter as FragmentStatePagerAdapter).getItem(viewPager?.currentItem!!)
        fragment.onActivityResult(requestCode, resultCode, data)
    }
}