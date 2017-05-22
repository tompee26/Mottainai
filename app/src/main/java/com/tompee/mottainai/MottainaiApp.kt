package com.tompee.mottainai

import android.app.Application
import io.realm.Realm

class MottainaiApp : Application() {
    companion object {
        val AUTH_URL = "http://54.178.162.237:9080/auth"
        val REALM_URL = "realm://54.178.162.237:9080/mottainai"
    }

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}