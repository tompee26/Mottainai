package com.tompee.mottainai

import android.app.Application
import io.realm.Realm

class MottainaiApp : Application() {
    companion object {
        val AUTH_URL = "http://54.65.167.222:9080/auth"
        val REALM_URL = "realm://54.65.167.222:9080/~/realmtasks"
    }

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}