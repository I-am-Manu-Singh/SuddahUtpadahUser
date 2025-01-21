package com.neatroots.suddahutpadah.fragmentauth

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.neatroots.suddahutpadah.R
import com.neatroots.suddahutpadah.databinding.FragmentForgetPasswordBinding
import com.neatroots.suddahutpadah.factory.AuthViewModelFactory
import com.neatroots.suddahutpadah.repository.AuthRepository
import com.neatroots.suddahutpadah.utils.Utils
import com.neatroots.suddahutpadah.viewmodel.AuthViewModel


class ForgetPasswordFragment : Fragment() {
    private val binding by lazy { FragmentForgetPasswordBinding.inflate(layoutInflater) }
    private lateinit var progress: AlertDialog
    private lateinit var authViewModel: AuthViewModel
    private lateinit var authRepository: AuthRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progress = Utils.showLoading(requireContext())

        authRepository = AuthRepository(FirebaseAuth.getInstance(), requireContext())
        val factory = AuthViewModelFactory(authRepository)
        authViewModel = ViewModelProvider(requireActivity(), factory)[AuthViewModel::class.java]

        authViewModel.emailRegistered.observe(viewLifecycleOwner) { success ->
            if(success) {
                authViewModel.sendPasswordReset(binding.etEmail.text.toString())
            } else {
                progress.dismiss()
                Utils.showMessage(requireContext(), "Email Not Fount")
            }
        }

        authViewModel.forgetPassStatus.observe(viewLifecycleOwner) { success ->
            if(success) {
                findNavController().navigate(R.id.action_forgetPasswordFragment_to_loginFragment)
                progress.dismiss()
                Utils.showMessage(requireContext(), "Password Reset link Sent")
            } else {
                progress.dismiss()
                Utils.showMessage(requireContext(), "Reset link Sent Failed")
            }

        }


        binding.apply {
            binding.btSendLink.setOnClickListener {
                progress.show()
                authViewModel.checkIfEmailRegistered(etEmail.text.toString())
            }

        }

    }

}