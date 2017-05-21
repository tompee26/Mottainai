package com.tompee.mottainai.controller.auth

import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton

abstract class FacebookAuth(private val loginButton: LoginButton) {
    private val TAG = "FacebookAuth"
    private val callbackManager: CallbackManager = CallbackManager.Factory.create()

    init {
        loginButton.setReadPermissions("email")

        // Callback registration
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

            override fun onSuccess(loginResult: LoginResult) {
                onRegistrationComplete(loginResult)
            }

            override fun onCancel() {
                onAuthCancelled()
            }

            override fun onError(exception: FacebookException) {
                onAuthError()
            }
        })

    }

    /**
     * Called if the authentication is cancelled by the user.

     * Adapter method, developer might want to override this method  to provide
     * custom logic.
     */
    fun onAuthCancelled() {
        Log.d(TAG, "onAuthCancelled")
    }

    /**
     * Called if the authentication fails.

     * Adapter method, developer might want to override this method  to provide
     * custom logic.
     */
    fun onAuthError() {
        Log.d(TAG, "onAuthError")
    }

    /**
     * Notify this class about the [FragmentActivity.onResume] event.
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * Called once we obtain a token from Facebook API.
     * @param loginResult contains the token obtained from Facebook API.
     */
    abstract fun onRegistrationComplete(loginResult: LoginResult)
}
