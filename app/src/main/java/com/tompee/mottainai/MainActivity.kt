package com.tompee.mottainai

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.tompee.mottainai.controller.adapter.MainPagerAdapter
import com.tompee.mottainai.controller.base.BaseActivity
import com.tompee.mottainai.model.Category
import io.realm.Realm
import io.realm.SyncConfiguration
import io.realm.SyncUser
import io.realm.permissions.PermissionChange
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState, true)

        if (SyncUser.currentUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
            return
        }
        setContentView(R.layout.activity_main)

        /** Sync with category realm */
        val config = SyncConfiguration.Builder(SyncUser.currentUser(), MottainaiApp.CATEGORY_URL).
                waitForInitialRemoteData().build()
        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm?) {
                val categories = realm?.where(Category::class.java)?.findAll()
                if (categories != null) {
                    MottainaiApp.categoryList.clear()
                    MottainaiApp.categoryList.addAll(categories)
                }
                viewpager.adapter = MainPagerAdapter(supportFragmentManager, this@MainActivity)
                viewpager.overScrollMode = View.OVER_SCROLL_NEVER
                tabLayout.setupWithViewPager(viewpager)
            }
        })
    }

    private fun createData() {
        var config = SyncConfiguration.Builder(SyncUser.currentUser(), MottainaiApp.CATEGORY_URL).waitForInitialRemoteData().build()
        val task = Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm?) {
                realm?.beginTransaction()
                val book = realm?.createObject(Category::class.java, "games")
                book?.imageUrl = "https://firebasestorage.googleapis.com/v0/b/mottainai-7a0d0.appspot.com/o/categories%2Fps4.jpg?alt=media&token=035af2a3-6c06-480f-bd9e-ea1422345cc6"
                realm?.commitTransaction()
            }
        })
    }

    private fun changePermission() {
        var realm = SyncUser.currentUser().managementRealm
        realm?.executeTransaction { realm ->
            val change = PermissionChange(MottainaiApp.CATEGORY_URL,
                    "*",
                    true,
                    false,
                    false)
            realm.insert(change)
        }
    }
}
