package com.neatroots.suddahutpadah.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.neatroots.suddahutpadah.databinding.FragmentProfileBinding
import com.neatroots.suddahutpadah.model.UserModel
import com.neatroots.suddahutpadah.utils.SharedPref


class ProfileFragment : Fragment() {
    private val binding by lazy { FragmentProfileBinding.inflate(layoutInflater) }
    private lateinit var user: UserModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user = SharedPref.getUserData(requireContext()) ?: UserModel()

        setData(user)

    }

    private fun setData(user: UserModel) {
        binding.apply {
            etHomeStayName.setText(user.name)
            etaddress1.setText(user.address)
            etCityName.setText(user.city)
            etEmailId.setText(user.email)
            etMobileNumber.setText(user.phoneNumber)
            etPincode.setText(user.pinCode)


        }
    }

}