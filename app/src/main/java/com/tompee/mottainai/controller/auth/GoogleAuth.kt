package com.tompee.mottainai.controller.auth

import android.content.Intent
import android.support.v4.app.FragmentActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.tompee.mottainai.R

abstract class GoogleAuth(btnSignIn: SignInButton, fragmentActivity: FragmentActivity) : GoogleApiClient.OnConnectionFailedListener {
    private val mGoogleApiClient: GoogleApiClient

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(fragmentActivity.getString(R.string.google_client_id))
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(fragmentActivity)
                .enableAutoManage(fragmentActivity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        btnSignIn.setOnClickListener {
            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
            fragmentActivity.startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    /**
     * Notify this class about the [FragmentActivity.onResume] event.
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleSignInResult(result)
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        if (!connectionResult.hasResolution()) {
            onError("Connection failed and has no resolution. code:" + connectionResult.errorCode)
        }
    }

    /**
     * Called once we obtain a token from Google Sign In API.
     * @param result contains the token obtained from Google Sign In API.
     */
    abstract fun onRegistrationComplete(result: GoogleSignInResult)

    /**
     * Called in case of authentication or other errors.

     * Adapter method, developer might want to override this method  to provide
     * custom logic.
     */
    open fun onError(s: String) {}

    private fun handleSignInResult(result: GoogleSignInResult) {
        if (result.isSuccess) {
            onRegistrationComplete(result)
        }
    }

    companion object {
        private val RC_SIGN_IN = 10
    }
}