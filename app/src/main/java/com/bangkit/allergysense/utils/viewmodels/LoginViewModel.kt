package com.bangkit.allergysense.utils.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit.allergysense.utils.preferences.User
import com.bangkit.allergysense.utils.repositories.AuthRepository

class LoginViewModel(private val authRepository: AuthRepository): ViewModel() {
    fun login(username: String, pass: String) = authRepository.log(username, pass)
    fun user(): LiveData<User> = authRepository.user()
}