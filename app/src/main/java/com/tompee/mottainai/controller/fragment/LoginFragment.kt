package com.tompee.mottainai.controller.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.tompee.mottainai.R

class LoginFragment : Fragment() {
    private var listener: LoginFragmentListener? = null

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

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is LoginFragmentListener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_login, container, false)
        val switchButton = view?.findViewById(R.id.button_switch) as Button
        val type = arguments?.getInt(TYPE_TAG)
        switchButton.setOnClickListener { listener?.onSwitchPage(type!!) }
        if (type == LOGIN) {
            switchButton.text = getString(R.string.label_login_new_account)
        } else {
            switchButton.text = getString(R.string.label_login_registered)
        }
        val commandButton = view.findViewById(R.id.button_command) as Button
        if (type == LOGIN) {
            commandButton.text = getString(R.string.label_login_button)
            commandButton.setBackgroundResource(R.drawable.ripple_login)
        } else {
            commandButton.text = getString(R.string.label_login_sign_up)
            commandButton.setBackgroundResource(R.drawable.ripple_sign_up)
        }
        return view
    }

    interface LoginFragmentListener {
        fun onSwitchPage(type: Int)
    }
}