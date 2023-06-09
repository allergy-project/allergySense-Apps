package com.bangkit.allergysense.utils.viewmodels

import androidx.lifecycle.ViewModel
import com.bangkit.allergysense.utils.repositories.AuthRepository

class LogoutViewModel(private val authRepository: AuthRepository): ViewModel() {
    fun logout() = authRepository.logout()
}