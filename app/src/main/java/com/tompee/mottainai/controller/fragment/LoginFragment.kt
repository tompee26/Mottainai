package com.tompee.mottainai.controller.fragment

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.tompee.mottainai.MainActivity
import com.tompee.mottainai.MottainaiApp
import com.tompee.mottainai.MottainaiApp.Companion.AUTH_URL
import com.tompee.mottainai.R
import com.tompee.mottainai.controller.auth.FacebookAuth
import com.tompee.mottainai.controller.auth.GoogleAuth
import com.tompee.mottainai.controller.auth.UserManager
import com.tompee.mottainai.model.Category
import io.realm.*
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.regex.Pattern
import kotlin.properties.Delegates

class LoginFragment : Fragment(), View.OnClickListener, SyncUser.Callback {
    private var listener: LoginFragmentListener by Delegates.notNull()
    private var progressDialog: ProgressDialog by Delegates.notNull()

    private var facebookAuth: FacebookAuth? = null
    private var googleAuth: GoogleAuth? = null

    companion object {
        const val EMAIL_PATTERN = "[A-Z0-9a-z_%+-]+(\\.[A-Z0-9a-z_%+-]+)*@[A-Za-z0-9]+([.-][A-Za-z0-9]+)*\\.[A-Za-z]{2,}"
        const val MIN_PASS_CHAR = 6

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

        private fun createData() {
            val realm = Realm.getDefaultInstance()
            realm.use { realm ->
                if (realm.where(Category::class.java).count() != 0L) {
                    return
                }
                realm.executeTransaction { realm ->
                    if (realm.where(Category::class.java).count() == 0L) {
                        val book = realm.createObject(Category::class.java, "book")
                        book.imageUrl = "something.com"
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is LoginFragmentListener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val type = arguments?.getInt(TYPE_TAG)
        switchButton.setOnClickListener { listener.onSwitchPage(type!!) }
        if (type == LOGIN) {
            switchButton.text = getString(R.string.label_login_new_account)
        } else {
            switchButton.text = getString(R.string.label_login_registered)
        }
        commandButton.setOnClickListener(this)

        if (type == LOGIN) {
            progressDialog = ProgressDialog(context, R.style.AppTheme_Login_Dialog)
            commandButton.text = getString(R.string.label_login_button)
            commandButton.setBackgroundResource(R.drawable.ripple_login)
            facebookAuth = object : FacebookAuth(facebookbButton) {
                override fun onRegistrationComplete(loginResult: LoginResult) {
                    UserManager.mode = UserManager.AUTH_MODE.FACEBOOK
                    val credentials = SyncCredentials.facebook(loginResult.accessToken.token)
                    SyncUser.loginAsync(credentials, AUTH_URL, this@LoginFragment)
                }
            }

            googleAuth = object : GoogleAuth(googleButton, activity) {
                override fun onRegistrationComplete(result: GoogleSignInResult) {
                    UserManager.mode = UserManager.AUTH_MODE.GOOGLE
                    val acct = result.signInAccount
                    val credentials = SyncCredentials.google(acct?.idToken!!)
                    SyncUser.loginAsync(credentials, AUTH_URL, this@LoginFragment)
                }

                override fun onError(s: String) {
                    super.onError(s)
                }
            }
        } else {
            progressDialog = ProgressDialog(context, R.style.AppTheme_SignUp_Dialog)
            commandButton.text = getString(R.string.label_login_sign_up)
            commandButton.setBackgroundResource(R.drawable.ripple_sign_up)
            googleButton.visibility = View.GONE
            facebookbButton.visibility = View.GONE
            optionTextView.visibility = View.GONE
            leftLineView.visibility = View.GONE
            rightLineView.visibility = View.GONE
        }
        progressDialog.isIndeterminate = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        facebookAuth?.onActivityResult(requestCode, resultCode, data!!)
        googleAuth?.onActivityResult(requestCode, resultCode, data!!)
    }

    override fun onClick(v: View?) {
        val type = arguments?.getInt(TYPE_TAG)
        userView.error = null
        passView.error = null
        if (!validateEmailField() || !validatePassField()) {
            return
        }
        if (type == SIGN_UP) {
            progressDialog.setMessage(getString(R.string.progress_login_register))
            progressDialog.show()
            SyncUser.loginAsync(SyncCredentials.usernamePassword(userView.text.toString(),
                    passView.text.toString(), true), AUTH_URL, object : SyncUser.Callback {
                override fun onSuccess(user: SyncUser) {
                    progressDialog.dismiss()
                    moveToMainActivity()
                }

                override fun onError(error: ObjectServerError) {
                    progressDialog.dismiss()
                    val errorMsg: String
                    when (error.errorCode) {
                        ErrorCode.EXISTING_ACCOUNT -> errorMsg = "Account already exists"
                        else -> errorMsg = error.toString()
                    }
                    Toast.makeText(this@LoginFragment.context, errorMsg, Toast.LENGTH_LONG).show()
                }
            })
        } else {
            progressDialog.setMessage(getString(R.string.progress_login_authenticate))
            progressDialog.show()
            SyncUser.loginAsync(SyncCredentials.usernamePassword(userView.text.toString(),
                    passView.text.toString(), false), MottainaiApp.AUTH_URL, this)
        }
    }

    private fun validateEmailField(): Boolean {
        val email = userView.text.toString()

        if (TextUtils.isEmpty(email)) {
            userView.error = getString(R.string.error_login_required)
            userView.requestFocus()
            return false
        }
        val ptn = Pattern.compile(EMAIL_PATTERN)
        val mc = ptn.matcher(email)
        if (!mc.matches()) {
            userView.error = getString(R.string.error_login_invalid_email)
            userView.requestFocus()
            return false
        }
        return true
    }

    private fun validatePassField(): Boolean {
        val pass = passView.text.toString()
        if (TextUtils.isEmpty(pass)) {
            passView.error = getString(R.string.error_login_required)
            passView.requestFocus()
            return false
        } else if (pass.length < MIN_PASS_CHAR) {
            passView.error = getString(R.string.error_login_pass_min)
            passView.requestFocus()
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
        progressDialog.dismiss()
        moveToMainActivity()
    }

    override fun onError(error: ObjectServerError?) {
        progressDialog.dismiss()
        val errorMsg: String
        when (error?.errorCode) {
            ErrorCode.UNKNOWN_ACCOUNT -> errorMsg = "Account does not exists."
            ErrorCode.INVALID_CREDENTIALS -> errorMsg = "The provided credentials are invalid!"
            else -> errorMsg = error.toString()
        }
        Toast.makeText(this@LoginFragment.context, errorMsg, Toast.LENGTH_LONG).show()
    }
}
