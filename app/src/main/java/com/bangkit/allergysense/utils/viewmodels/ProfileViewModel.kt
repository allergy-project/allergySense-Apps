package com.bangkit.allergysense.utils.viewmodels

import androidx.lifecycle.ViewModel
import com.bangkit.allergysense.utils.repositories.AllergyRepository

class ProfileViewModel(private val allergyRepository: AllergyRepository): ViewModel() {
    fun getProfile(auth: String) = allergyRepository.getProfile(auth)
}