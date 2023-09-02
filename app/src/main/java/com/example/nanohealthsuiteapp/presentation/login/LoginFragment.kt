package com.example.nanohealthsuiteapp.presentation.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.nanohealthsuiteapp.BuildConfig
import com.example.nanohealthsuiteapp.R
import com.example.nanohealthsuiteapp.common.Constants.SOMETHING_WRONG
import com.example.nanohealthsuiteapp.common.Constants.USER_TOKEN
import com.example.nanohealthsuiteapp.common.Resource
import com.example.nanohealthsuiteapp.utils.SharedPrefHelper
import com.example.nanohealthsuiteapp.utils.isValidEmail
import com.example.nanohealthsuiteapp.utils.isValidPassword
import com.example.nanohealthsuiteapp.utils.makeInvisible
import com.example.nanohealthsuiteapp.utils.makeVisible
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    private var ivEmailVerification: ImageView? = null
    private var ivShowPassword: ImageView? = null
    private var tvContinue: TextView? = null
    private var loginProgress: ProgressBar? = null

    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(this)[LoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initListeners()

    }

    private fun init() {

        etEmail = requireView().findViewById(R.id.et_email)
        etPassword = requireView().findViewById(R.id.et_password)
        ivEmailVerification = requireView().findViewById(R.id.iv_email_verification)
        ivShowPassword = requireView().findViewById(R.id.iv_show_password)
        tvContinue = requireView().findViewById(R.id.tv_continue)
        loginProgress = requireView().findViewById(R.id.login_progress)

        if (BuildConfig.DEBUG) {
            etEmail?.setText(BuildConfig.USER_NAME)
            etPassword?.setText(BuildConfig.PASSWORD)
        }

    }

    private fun initListeners() {
        lifecycleScope.launch(Dispatchers.Main) {
            loginViewModel.userLoginFlow.collect {
                when (it) {
                    is Resource.Error -> {
                        loginProgress?.makeInvisible()
                        tvContinue?.makeVisible()
                        Snackbar.make(
                            requireContext(),
                            requireView(),
                            it.message ?: SOMETHING_WRONG,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    is Resource.Loading -> {
                        loginProgress?.makeVisible()
                        tvContinue?.makeInvisible()
                    }

                    is Resource.Success -> {
                        val token = it.data?.token
                        loginProgress?.makeInvisible()
                        tvContinue?.makeVisible()
                        if (!token.isNullOrEmpty()) {
                            SharedPrefHelper.writeString(requireContext(), USER_TOKEN, token)
                            Snackbar.make(
                                requireContext(),
                                requireView(),
                                "Login Successful",
                                Snackbar.LENGTH_LONG
                            ).show()
                            findNavController().popBackStack()
                        } else {
                            Snackbar.make(
                                requireContext(),
                                requireView(),
                                SOMETHING_WRONG,
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }

        etEmail?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (!p0.isNullOrEmpty()) {
                    if (isValidEmail(p0.toString().trim())) {
                        ivEmailVerification?.makeVisible()
                    } else {
                        ivEmailVerification?.makeInvisible()
                    }
                }
            }
        })

        tvContinue?.setOnClickListener {
            val username = etEmail?.text.toString().trim()
            val password = etPassword?.text.toString().trim()

            if (username.length < 6) {
                etEmail?.error = "Please enter valid email"
                return@setOnClickListener
            }
            if (!isValidPassword(password)) {
                etPassword?.error = "Password must be 6 or more characters long"
                return@setOnClickListener
            }

            loginViewModel.login(username, password)
        }

        ivShowPassword?.setOnClickListener {
            etPassword?.let {
                if (it.inputType == (android.text.InputType.TYPE_CLASS_TEXT or
                            android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD)
                ) {
                    it.inputType = android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    ivShowPassword?.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_hide_password
                        )
                    )
                } else {
                    ivShowPassword?.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_show_password
                        )
                    )
                    it.inputType = android.text.InputType.TYPE_CLASS_TEXT or
                            android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
            }
        }

    }
}