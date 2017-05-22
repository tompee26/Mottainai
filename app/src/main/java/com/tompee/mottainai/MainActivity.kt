package com.tompee.mottainai

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.View
import com.tompee.mottainai.controller.adapter.MainPagerAdapter
import com.tompee.mottainai.controller.auth.UserManager
import com.tompee.mottainai.controller.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState, true)

//        val realm = Realm.getDefaultInstance()
//        realm.executeTransaction({ realm ->
//            val book = realm.createObject(Category::class.java, "Book")
//            book.imageUrl = "https://firebasestorage.googleapis.com/v0/b/mottainai-7a0d0.appspot.com/o/categories%2Fbooks.jpg?alt=media&token=cdd51051-1c2f-46d2-b74f-b1f108eeec9d"
//        })
//
//        realm.executeTransaction { realm ->
//            val allValues = realm.where(Category::class.java).findAll()
//        }

        val user = UserManager.getActiveUser()
        if (user == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        } else {
            UserManager.setActiveUser(user)
        }
        setContentView(R.layout.activity_main)

        val viewPager = findViewById(R.id.view_pager) as ViewPager
        viewPager.adapter = MainPagerAdapter(supportFragmentManager, this)
        viewPager.overScrollMode = View.OVER_SCROLL_NEVER

        (findViewById(R.id.tab_layout) as TabLayout).setupWithViewPager(viewPager)
    }
}
