package com.tompee.mottainai.controller.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FirebaseAuth
import com.tompee.mottainai.MainActivity
import com.tompee.mottainai.MottainaiApp.Companion.AUTH_URL
import com.tompee.mottainai.R
import com.tompee.mottainai.controller.auth.FacebookAuth
import com.tompee.mottainai.controller.auth.UserManager
import io.realm.ErrorCode
import io.realm.ObjectServerError
import io.realm.SyncCredentials
import io.realm.SyncUser
import java.util.regex.Pattern

class LoginFragment : Fragment(), View.OnClickListener, SyncUser.Callback {
    private val EMAIL_PATTERN = "[A-Z0-9a-z_%+-]+(\\.[A-Z0-9a-z_%+-]+)*@[A-Za-z0-9]+([.-][A-Za-z0-9]+)*\\.[A-Za-z]{2,}"
    private val MIN_PASS_CHAR = 6
    private var listener: LoginFragmentListener? = null
    private var auth: FirebaseAuth? = null
    private var userView: AutoCompleteTextView? = null
    private var passView: AutoCompleteTextView? = null

    private var facebookAuth: FacebookAuth? = null

    companion object {
        const val LOGIN = 0
        const val SIGN_UP = 1
        private const val TYPE_TAG = "type"

        fun newInstance(type: Int): LoginFragment {
            val loginFragment = LoginFragment()
            val bundle = Bundle()
            bundle.putInt(TYPE_TAG, type)
            loginFragment.arguments = bundle
            return loginFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is LoginFragmentListener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_login, container, false)
        userView = view?.findViewById(R.id.et_user) as AutoCompleteTextView
        passView = view.findViewById(R.id.et_pass) as AutoCompleteTextView

        val switchButton = view.findViewById(R.id.button_switch) as Button
        val type = arguments?.getInt(TYPE_TAG)
        switchButton.setOnClickListener { listener?.onSwitchPage(type!!) }
        if (type == LOGIN) {
            switchButton.text = getString(R.string.label_login_new_account)
        } else {
            switchButton.text = getString(R.string.label_login_registered)
        }
        val commandButton = view.findViewById(R.id.button_command) as Button
        commandButton.setOnClickListener(this)

        facebookAuth = object : FacebookAuth(view.findViewById(R.id.facebook_login_button) as LoginButton) {
            override fun onRegistrationComplete(loginResult: LoginResult) {
                UserManager.mode = UserManager.AUTH_MODE.FACEBOOK
                val credentials = SyncCredentials.facebook(loginResult.accessToken.token)
                SyncUser.loginAsync(credentials, AUTH_URL, this@LoginFragment)
            }
        }

        if (type == LOGIN) {
            commandButton.text = getString(R.string.label_login_button)
            commandButton.setBackgroundResource(R.drawable.ripple_login)
        } else {
            commandButton.text = getString(R.string.label_login_sign_up)
            commandButton.setBackgroundResource(R.drawable.ripple_sign_up)
            view.findViewById(R.id.google_sign_in_button).visibility = View.GONE
            view.findViewById(R.id.facebook_login_button).visibility = View.GONE
            view.findViewById(R.id.tv_option).visibility = View.GONE
            view.findViewById(R.id.left_line).visibility = View.GONE
            view.findViewById(R.id.right_line).visibility = View.GONE
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        facebookAuth?.onActivityResult(requestCode, resultCode, data!!)
    }

    override fun onClick(v: View?) {
        val type = arguments?.getInt(TYPE_TAG)

        if (!validateEmailField() || !validatePassField()) {
            return
        }
        if (type == SIGN_UP) {
            auth?.createUserWithEmailAndPassword(userView?.text.toString(),
                    passView?.text.toString())?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth?.currentUser?.sendEmailVerification()
                } else {
                    Log.d("hello", "Sign up failed")
                }
            }
        } else {
            auth?.signInWithEmailAndPassword(userView?.text.toString(),
                    passView?.text.toString())?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (auth?.currentUser?.isEmailVerified!!) {
                        moveToMainActivity()
                    } else {
                        Log.d("hello", "Verify email first")
                    }
                } else {
                    Log.d("hello", "Log in failed")
                }
            }
        }
    }

    private fun validateEmailField(): Boolean {
        val email = userView?.text.toString()

        if (TextUtils.isEmpty(email)) {
            userView?.error = getString(R.string.error_login_required)
            userView?.requestFocus()
            return false
        }
        val ptn = Pattern.compile(EMAIL_PATTERN)
        val mc = ptn.matcher(email)
        if (!mc.matches()) {
            userView?.error = getString(R.string.error_login_invalid_email)
            userView?.requestFocus()
            return false
        }
        return true
    }

    private fun validatePassField(): Boolean {
        val pass = passView?.text.toString()
        if (TextUtils.isEmpty(pass)) {
            passView?.error = getString(R.string.error_login_required)
            passView?.requestFocus()
            return false
        } else if (pass.length < MIN_PASS_CHAR) {
            passView?.error = getString(R.string.error_login_pass_min)
            passView?.requestFocus()
            return false
        }
        return true
    }

    private fun moveToMainActivity() {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        activity.finish()
    }

    interface LoginFragmentListener {
        fun onSwitchPage(type: Int)
    }

    override fun onSuccess(user: SyncUser?) {
//        showProgress(false)
        loginComplete(user)
    }

    override fun onError(error: ObjectServerError?) {
//        showProgress(false)
        val errorMsg: String
        when (error?.errorCode) {
            ErrorCode.UNKNOWN_ACCOUNT -> errorMsg = "Account does not exists."
            ErrorCode.INVALID_CREDENTIALS -> errorMsg = "The provided credentials are invalid!"
            else -> errorMsg = error.toString()
        }
        Toast.makeText(this@LoginFragment.context, errorMsg, Toast.LENGTH_LONG).show()
    }

    private fun loginComplete(user: SyncUser?) {
        UserManager.setActiveUser(user!!)

        val mainActivity = Intent(context, MainActivity::class.java)
        mainActivity.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(mainActivity)
        activity.finish()
    }
}