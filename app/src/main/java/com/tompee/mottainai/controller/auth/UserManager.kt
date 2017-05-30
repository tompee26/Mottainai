package com.tompee.mottainai.controller.auth

import android.content.Context
import com.facebook.login.LoginManager
import com.tompee.mottainai.MottainaiApp
import io.realm.Realm
import io.realm.SyncConfiguration
import io.realm.SyncUser
import java.io.IOException

object UserManager {

    enum class AUTH_MODE {
        PASSWORD,
        FACEBOOK,
        GOOGLE
    }

    var mode = AUTH_MODE.PASSWORD

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
}