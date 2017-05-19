package com.tompee.mottainai.controller.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.tompee.mottainai.MainActivity
import com.tompee.mottainai.R
import java.util.regex.Pattern


class LoginFragment : Fragment(), View.OnClickListener {
    private val EMAIL_PATTERN = "[A-Z0-9a-z_%+-]+(\\.[A-Z0-9a-z_%+-]+)*@[A-Za-z0-9]+([.-][A-Za-z0-9]+)*\\.[A-Za-z]{2,}"
    private val MIN_PASS_CHAR = 6
    private var listener: LoginFragmentListener? = null
    private var auth: FirebaseAuth? = null
    private var userView: EditText? = null
    private var passView: EditText? = null
    private var userViewLabel: TextView? = null
    private var passViewLabel: TextView? = null
    private var userViewUnderline: View? = null
    private var passViewUnderline: View? = null

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
        userView = view?.findViewById(R.id.et_user) as EditText
        passView = view.findViewById(R.id.et_pass) as EditText
        userViewLabel = view.findViewById(R.id.tv_user_label) as TextView
        passViewLabel = view.findViewById(R.id.tv_pass_label) as TextView
        userViewUnderline = view.findViewById(R.id.view_user_underline)
        passViewUnderline = view.findViewById(R.id.view_pass_underline)

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
        if (type == LOGIN) {
            commandButton.text = getString(R.string.label_login_button)
            commandButton.setBackgroundResource(R.drawable.ripple_login)
        } else {
            commandButton.text = getString(R.string.label_login_sign_up)
            commandButton.setBackgroundResource(R.drawable.ripple_sign_up)
        }
        return view
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

        if (email.isEmpty()) {
            userViewLabel?.setTextColor(Color.RED)
            userViewUnderline?.setBackgroundColor(Color.RED)
            return false
        }

        val ptn = Pattern.compile(EMAIL_PATTERN)
        val mc = ptn.matcher(email)
        if (!mc.matches()) {
            userViewLabel?.setTextColor(Color.RED)
            userViewUnderline?.setBackgroundColor(Color.RED)
            return false
        }

        userViewLabel?.setTextColor(ContextCompat.getColor(context, R.color.colorLight))
        userViewUnderline?.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLight))
        return true
    }

    private fun validatePassField(): Boolean {
        val pass = passView?.text.toString()
        if (pass.isEmpty()) {
            passViewLabel?.setTextColor(Color.RED)
            passViewUnderline?.setBackgroundColor(Color.RED)
            return false
        } else if (pass.length < MIN_PASS_CHAR) {
            passViewLabel?.setTextColor(Color.RED)
            passViewUnderline?.setBackgroundColor(Color.RED)
            return false
        } else {
            passViewLabel?.setTextColor(ContextCompat.getColor(context, R.color.colorLight))
            passViewUnderline?.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLight))
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
}