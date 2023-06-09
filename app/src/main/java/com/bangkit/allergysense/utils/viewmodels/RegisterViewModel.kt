package com.bangkit.allergysense.utils.viewmodels

import androidx.lifecycle.ViewModel
import com.bangkit.allergysense.utils.repositories.AuthRepository

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun reg(username :String, email: String, pass: String) = authRepository.reg(username, email, pass)
}