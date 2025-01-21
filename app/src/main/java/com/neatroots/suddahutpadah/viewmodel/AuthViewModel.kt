package com.neatroots.suddahutpadah.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neatroots.suddahutpadah.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository): ViewModel() {

    val authStatus: LiveData<Boolean> = authRepository.authStatus
    val verificationLink: LiveData<Boolean> = authRepository.verificationLink
    val loginStatus: LiveData<Boolean> = authRepository.loginStatus
    val forgetPassStatus: LiveData<Boolean> = authRepository.forgetPassStatus
    val emailRegistered: LiveData<Boolean> = authRepository.emailRegistered



    fun registerUser(name: String, phoneNumber: String, email: String, password: String, address: String, location: String, city: String, pinCode: String, verified: Boolean, active: Boolean) {
        viewModelScope.launch {
            try {
                authRepository.signUpWithEmail(name, phoneNumber, email, password, address, location, city, pinCode, verified, active)
            } catch (e: Exception) {
                authRepository.authStatus.postValue(false)
            }
        }
    }



    fun singInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            try {
                authRepository.signInWithEmail(email, password)
            } catch (e: Exception) {
                authRepository.loginStatus.postValue(false)
            }
        }

    }

    fun checkIfEmailRegistered(email: String) {
        viewModelScope.launch {
            try {
                authRepository.isEmailAvailable(email)
            } catch (e: Exception) {
                authRepository.forgetPassStatus.postValue(false)
            }
        }
    }

    fun sendPasswordReset(email: String) {
        viewModelScope.launch {
            try {
                authRepository.sendPasswordResetEmail(email)
            } catch (e: Exception) {
                authRepository.forgetPassStatus.postValue(false)
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
    }

    fun sendVerificationLink() {
        authRepository.sendVerificationEmail()
    }

}