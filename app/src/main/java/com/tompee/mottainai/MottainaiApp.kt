package com.tompee.mottainai

import android.app.Application
import com.tompee.mottainai.model.Category
import io.realm.Realm

class MottainaiApp : Application() {
    companion object {
        val AUTH_URL = "http://52.198.32.185:9080/auth"
        val CATEGORY_URL = "realm://52.198.32.185:9080/7aa943a1acd6b4728587f8108dd05b87/category"
        val REALM_URL = "realm://52.198.32.185:9080/~/default"

        val categoryList : MutableList<Category> = ArrayList()
    }

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}