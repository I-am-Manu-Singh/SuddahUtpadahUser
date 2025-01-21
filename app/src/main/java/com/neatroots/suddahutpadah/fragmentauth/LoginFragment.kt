package com.neatroots.suddahutpadah.fragmentauth

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.neatroots.suddahutpadah.R
import com.neatroots.suddahutpadah.activities.HomeMainActivity
import com.neatroots.suddahutpadah.databinding.FragmentLoginBinding
import com.neatroots.suddahutpadah.factory.AuthViewModelFactory
import com.neatroots.suddahutpadah.repository.AuthRepository
import com.neatroots.suddahutpadah.utils.Utils
import com.neatroots.suddahutpadah.viewmodel.AuthViewModel


class LoginFragment : Fragment() {
    private val binding by lazy { FragmentLoginBinding.inflate(layoutInflater) }
    private lateinit var authViewModel: AuthViewModel
    private lateinit var progress: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progress = Utils.showLoading(requireContext())

        val authRepository = AuthRepository(FirebaseAuth.getInstance(), requireContext())
        val factory = AuthViewModelFactory(authRepository)
        authViewModel = ViewModelProvider(requireActivity(), factory)[AuthViewModel::class.java]

        authViewModel.loginStatus.observe(viewLifecycleOwner) { success ->

            val user = Firebase.auth.currentUser
            val emailVerified = user?.isEmailVerified ?: false
            if(success) {
                if(emailVerified) {
                    startActivity(Intent(requireActivity(), HomeMainActivity::class.java))
                    requireActivity().finish()
                    progress.dismiss()
                } else {
                    progress.dismiss()
                    Utils.showMessage(requireContext(), "Verify Your Email")
                }
            } else {
                progress.dismiss()
                Utils.showMessage(requireContext(), "Please Check Email & Password")
            }

        }


        binding.apply {

            btLogin.setOnClickListener {
                validateLogin()
            }

            tvCreateAc.setOnClickListener {
                Utils.navigate(it, R.id.action_loginFragment_to_registerFragment)
            }

            tvCreateAccount.setOnClickListener {
                Utils.navigate(it, R.id.action_loginFragment_to_registerFragment)
            }

            tvForgetPassword.setOnClickListener {
                Utils.navigate(it, R.id.action_loginFragment_to_forgetPasswordFragment)
            }


        }


    }

    private fun validateLogin() {
        binding.apply {
            val message = "Empty"
            if (etEmail.text.toString().isEmpty()) {
                etEmail.requestFocus()
                etEmail.error = message
            } else if (etPassword.text.toString().isEmpty()) {
                etPassword.requestFocus()
                etPassword.error = message
            } else {
                progress.show()
                authViewModel.singInWithEmail(etEmail.text.toString(), etPassword.text.toString())
            }
        }
    }


}