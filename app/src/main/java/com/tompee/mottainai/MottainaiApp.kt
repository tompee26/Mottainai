package com.tompee.mottainai

import android.app.Application
import io.realm.Realm

class MottainaiApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}