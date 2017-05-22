package com.tompee.mottainai.controller.auth

import com.facebook.login.LoginManager
import com.tompee.mottainai.MottainaiApp
import io.realm.Realm
import io.realm.SyncConfiguration
import io.realm.SyncUser

object UserManager {

    enum class AUTH_MODE {
        PASSWORD,
        FACEBOOK,
        GOOGLE
    }

    var mode = AUTH_MODE.PASSWORD // default

    fun logoutActiveUser() {
        when (mode) {
            UserManager.AUTH_MODE.PASSWORD -> {
            }
            UserManager.AUTH_MODE.FACEBOOK -> {
                LoginManager.getInstance().logOut()
            }
            UserManager.AUTH_MODE.GOOGLE -> {
            }
        }
        SyncUser.currentUser().logout()
    }

    fun setActiveUser(user: SyncUser) {
        val defaultConfig = SyncConfiguration.Builder(user, MottainaiApp.REALM_URL).build()
        Realm.setDefaultConfiguration(defaultConfig)
    }

    fun getActiveUser() : SyncUser? {
        return SyncUser.currentUser()
    }
}